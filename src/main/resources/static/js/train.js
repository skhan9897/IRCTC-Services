/* =========================================
   TRAIN.JS
   Railway Booking System
========================================= */

let trainResults = [];


/* =========================================
   INITIALIZE
========================================= */

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadSavedSearch();

        initializeTrainSearch();

        setMinimumDate();

    }
);


/* =========================================
   MINIMUM DATE
========================================= */

function setMinimumDate() {

    const dateInput =
        document.getElementById(
            "journeyDate"
        );


    if (!dateInput) {
        return;
    }


    const today =
        new Date()
            .toISOString()
            .split("T")[0];


    dateInput.min =
        today;
}


/* =========================================
   LOAD SAVED SEARCH
========================================= */

function loadSavedSearch() {

    const from =
        localStorage.getItem(
            "from"
        );


    const to =
        localStorage.getItem(
            "to"
        );


    const journeyDate =
        localStorage.getItem(
            "journeyDate"
        );


    const fromInput =
        document.getElementById(
            "from"
        );


    const toInput =
        document.getElementById(
            "to"
        );


    const dateInput =
        document.getElementById(
            "journeyDate"
        );


    if (fromInput && from) {

        fromInput.value =
            from;
    }


    if (toInput && to) {

        toInput.value =
            to;
    }


    if (
        dateInput &&
        journeyDate
    ) {

        dateInput.value =
            journeyDate;
    }
}


/* =========================================
   SEARCH FORM
========================================= */

function initializeTrainSearch() {

    const form =
        document.getElementById(
            "trainSearchForm"
        );


    if (!form) {
        return;
    }


    form.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();

            await searchTrains();

        }
    );
}


/* =========================================
   SEARCH TRAINS
========================================= */

async function searchTrains() {

    const from =
        getValue("from");


    const to =
        getValue("to");


    const journeyDate =
        getValue("journeyDate");


    /* =========================
       VALIDATION
    ========================= */

    if (
        !from ||
        !to ||
        !journeyDate
    ) {

        showTrainMessage(
            "Please enter From, To and Journey Date.",
            "error"
        );

        return;
    }


    if (
        from.toLowerCase() ===
        to.toLowerCase()
    ) {

        showTrainMessage(
            "From and To stations cannot be same.",
            "error"
        );

        return;
    }


    /* =========================
       SAVE SEARCH
    ========================= */

    localStorage.setItem(
        "from",
        from
    );


    localStorage.setItem(
        "to",
        to
    );


    localStorage.setItem(
        "journeyDate",
        journeyDate
    );


    showLoading();


    try {

        /*
         * Preferred API:
         *
         * TrainScheduleController
         *
         * From + To + Date
         */

        let trains;


        try {

            trains =
                await API.searchSchedules(
                    from,
                    to,
                    journeyDate
                );

        } catch (scheduleError) {

            /*
             * Fallback:
             *
             * If schedule API is not
             * available, use Train API.
             */

            console.warn(
                "Schedule API failed. Using train search.",
                scheduleError
            );


            trains =
                await API.searchTrains(
                    from,
                    to
                );
        }


        trainResults =
            Array.isArray(trains)
                ? trains
                : [];


        displayTrains(
            trainResults
        );


    } catch (error) {

        console.error(
            "Train search error:",
            error
        );


        showTrainMessage(
            error.message ||
            "Unable to search trains.",
            "error"
        );
    }
}


/* =========================================
   DISPLAY TRAINS
========================================= */

function displayTrains(
    trains
) {

    const container =
        document.getElementById(
            "trainResults"
        );


    if (!container) {
        return;
    }


    if (
        !trains ||
        trains.length === 0
    ) {

        container.innerHTML = `

            <div class="empty-trains">

                <div class="empty-icon">
                    🚆
                </div>

                <h2>
                    No Trains Found
                </h2>

                <p>
                    No train is available
                    for this route.
                </p>

            </div>

        `;

        return;
    }


    container.innerHTML = "";


    trains.forEach(
        (train, index) => {

            const card =
                createTrainCard(
                    train,
                    index
                );


            container.appendChild(
                card
            );

        }
    );
}


/* =========================================
   CREATE TRAIN CARD
========================================= */

function createTrainCard(
    train,
    index
) {

    const card =
        document.createElement(
            "div"
        );


    card.className =
        "train-card";


    const trainName =
        train.trainName ||
        train.train?.trainName ||
        "Train";


    const trainNumber =
        train.trainNumber ||
        train.train?.trainNumber ||
        "-";


    const source =
        train.source ||
        train.fromStation ||
        train.train?.source ||
        localStorage.getItem(
            "from"
        ) ||
        "-";


    const destination =
        train.destination ||
        train.toStation ||
        train.train?.destination ||
        localStorage.getItem(
            "to"
        ) ||
        "-";


    const departure =
        train.departureTime ||
        train.departure ||
        "-";


    const arrival =
        train.arrivalTime ||
        train.arrival ||
        "-";


    const availableSeats =
        train.availableSeats ??
        train.seatsAvailable ??
        "-";


    const totalSeats =
        train.totalSeats ??
        "-";


    card.innerHTML = `

        <div class="train-top">

            <div>

                <h3>
                    ${escapeHtml(
        trainName
    )}
                </h3>

                <span>
                    Train No:
                    ${escapeHtml(
        String(
            trainNumber
        )
    )}
                </span>

            </div>

        </div>


        <div class="train-route">


            <div class="station">

                <small>
                    FROM
                </small>

                <strong>
                    ${escapeHtml(
        source
    )}
                </strong>

                <span>
                    ${escapeHtml(
        String(
            departure
        )
    )}
                </span>

            </div>


            <div class="route-arrow">
                →
            </div>


            <div class="station">

                <small>
                    TO
                </small>

                <strong>
                    ${escapeHtml(
        destination
    )}
                </strong>

                <span>
                    ${escapeHtml(
        String(
            arrival
        )
    )}
                </span>

            </div>


        </div>


        <div class="train-info">


            <div>
                <small>
                    Journey Date
                </small>

                <strong>
                    ${escapeHtml(
        localStorage.getItem(
            "journeyDate"
        ) || "-"
    )}
                </strong>
            </div>


            <div>
                <small>
                    Available Seats
                </small>

                <strong>
                    ${escapeHtml(
        String(
            availableSeats
        )
    )}
                </strong>
            </div>


            <div>
                <small>
                    Total Seats
                </small>

                <strong>
                    ${escapeHtml(
        String(
            totalSeats
        )
    )}
                </strong>
            </div>


        </div>


        <div class="train-classes">

            ${createClassButton(
        train,
        "SL",
        getFare(
            train,
            "SL"
        ),
        index
    )}


            ${createClassButton(
        train,
        "3A",
        getFare(
            train,
            "3A"
        ),
        index
    )}


            ${createClassButton(
        train,
        "2A",
        getFare(
            train,
            "2A"
        ),
        index
    )}


            ${createClassButton(
        train,
        "1A",
        getFare(
            train,
            "1A"
        ),
        index
    )}

        </div>

    `;


    return card;
}


/* =========================================
   GET FARE
========================================= */

function getFare(
    train,
    classType
) {

    /*
     * Different backend naming
     * possibilities are supported.
     */

    if (
        classType === "SL"
    ) {

        return (
            train.sleeperFare ??
            train.slFare ??
            train.sleeperPrice ??
            train.slPrice ??
            null
        );
    }


    if (
        classType === "3A"
    ) {

        return (
            train.ac3Fare ??
            train.thirdAcFare ??
            train.ac3Price ??
            train.threeAcFare ??
            null
        );
    }


    if (
        classType === "2A"
    ) {

        return (
            train.ac2Fare ??
            train.secondAcFare ??
            train.ac2Price ??
            train.twoAcFare ??
            null
        );
    }


    if (
        classType === "1A"
    ) {

        return (
            train.ac1Fare ??
            train.firstAcFare ??
            train.ac1Price ??
            train.firstAcPrice ??
            null
        );
    }


    return null;
}


/* =========================================
   CLASS BUTTON
========================================= */

function createClassButton(
    train,
    classType,
    fare,
    index
) {

    /*
     * If backend doesn't return fare,
     * still show class but disable it.
     */

    if (
        fare === null ||
        fare === undefined
    ) {

        return `
            <button
                class="class-btn disabled"
                disabled>

                <strong>
                    ${classType}
                </strong>

                <small>
                    Not Available
                </small>

            </button>
        `;
    }


    return `

        <button
            type="button"
            class="class-btn"
            onclick="
                selectTrain(
                    ${index},
                    '${classType}',
                    ${Number(fare)}
                )
            ">

            <strong>
                ${classType}
            </strong>

            <small>
                ₹${Number(fare)}
            </small>

            <span>
                Select
            </span>

        </button>

    `;
}


/* =========================================
   SELECT TRAIN
========================================= */

function selectTrain(
    index,
    classType,
    fare
) {

    const train =
        trainResults[index];


    if (!train) {

        alert(
            "Train information not found."
        );

        return;
    }


    /*
     * Important:
     *
     * Preserve database ID.
     */

    const trainId =
        train.id ||
        train.trainId ||
        train.train?.id;


    if (!trainId) {

        alert(
            "Train ID is missing from API response."
        );

        console.error(
            "Train object:",
            train
        );

        return;
    }


    const selectedTrain = {

        id:
        trainId,

        trainId:
        trainId,

        trainName:
            train.trainName ||
            train.train?.trainName ||
            "Train",

        trainNumber:
            train.trainNumber ||
            train.train?.trainNumber ||
            "",

        source:
            train.source ||
            train.fromStation ||
            train.train?.source ||
            localStorage.getItem(
                "from"
            ),

        destination:
            train.destination ||
            train.toStation ||
            train.train?.destination ||
            localStorage.getItem(
                "to"
            ),

        departureTime:
            train.departureTime ||
            train.departure ||
            "",

        arrivalTime:
            train.arrivalTime ||
            train.arrival ||
            "",

        totalSeats:
            train.totalSeats ??
            0,

        availableSeats:
            train.availableSeats ??
            train.seatsAvailable ??
            0

    };


    /*
     * Save selected train
     */

    localStorage.setItem(
        "selectedTrain",
        JSON.stringify(
            selectedTrain
        )
    );


    localStorage.setItem(
        "classType",
        classType
    );


    localStorage.setItem(
        "selectedFare",
        Number(fare)
    );


    /*
     * Go booking
     */

    window.location.href =
        "booking.html";
}


/* =========================================
   LOADING
========================================= */

function showLoading() {

    const container =
        document.getElementById(
            "trainResults"
        );


    if (!container) {
        return;
    }


    container.innerHTML = `

        <div class="loading">

            <div style="font-size:40px;">
                🚆
            </div>

            <h3>
                Searching Trains...
            </h3>

            <p>
                Please wait.
            </p>

        </div>

    `;
}


/* =========================================
   MESSAGE
========================================= */

function showTrainMessage(
    message,
    type = "error"
) {

    const container =
        document.getElementById(
            "trainResults"
        );


    if (!container) {

        alert(message);

        return;
    }


    container.innerHTML = `

        <div class="train-message ${type}">

            ${escapeHtml(
        message
    )}

        </div>

    `;
}


/* =========================================
   GET VALUE
========================================= */

function getValue(
    id
) {

    const element =
        document.getElementById(
            id
        );


    return element
        ? element.value.trim()
        : "";
}


/* =========================================
   ESCAPE HTML
========================================= */

function escapeHtml(
    value
) {

    return String(value)

        .replaceAll(
            "&",
            "&amp;"
        )

        .replaceAll(
            "<",
            "&lt;"
        )

        .replaceAll(
            ">",
            "&gt;"
        )

        .replaceAll(
            '"',
            "&quot;"
        )

        .replaceAll(
            "'",
            "&#039;"
        );
}