let allTrains = [];


/* =========================================
   INITIALIZE
========================================= */

document.addEventListener(
    "DOMContentLoaded",
    async function () {

        await loadTrains();

        initializeTrainSearch();

    }
);


/* =========================================
   LOAD TRAINS
========================================= */

async function loadTrains() {

    const container =
        document.getElementById(
            "trainsGrid"
        );

    try {

        allTrains =
            await API.getTrains();


        if (
            !Array.isArray(allTrains) ||
            allTrains.length === 0
        ) {

            container.innerHTML = `
                <div class="loading">
                    No trains found.
                </div>
            `;

            return;
        }


        displayTrains(
            allTrains
        );

    } catch (error) {

        console.error(
            "Train loading error:",
            error
        );


        container.innerHTML = `
            <div class="loading">
                Unable to load trains.
            </div>
        `;
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
            "trainsGrid"
        );


    container.innerHTML = "";


    if (!trains.length) {

        container.innerHTML = `
            <div class="loading">
                No matching trains found.
            </div>
        `;

        return;
    }


    trains.forEach(
        train => {

            const card =
                document.createElement(
                    "div"
                );


            card.className =
                "train-card";


            const trainId =
                train.id ??
                train.trainId;


            const trainName =
                train.trainName ||
                train.name ||
                "-";


            const trainNumber =
                train.trainNumber ||
                train.number ||
                "-";


            const source =
                train.source ||
                train.fromStation ||
                "-";


            const destination =
                train.destination ||
                train.toStation ||
                "-";


            const departure =
                train.departureTime ||
                train.departure ||
                "-";


            const arrival =
                train.arrivalTime ||
                train.arrival ||
                "-";


            const totalSeats =
                train.totalSeats ??
                "-";


            const availableSeats =
                train.availableSeats ??
                train.seatsAvailable ??
                "-";


            card.innerHTML = `

                <div class="train-header">

                    <div class="train-icon">
                        🚆
                    </div>


                    <div class="train-title">

                        <h3>
                            ${escapeHtml(
                trainName
            )}
                        </h3>

                        <span class="train-number">

                            Train No:
                            ${escapeHtml(
                String(
                    trainNumber
                )
            )}

                        </span>

                    </div>

                </div>


                <div class="route">

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


                <div class="train-details">

                    <div class="detail-box">

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


                    <div class="detail-box">

                        <small>
                            Available
                        </small>

                        <strong>
                            ${escapeHtml(
                String(
                    availableSeats
                )
            )}
                        </strong>

                    </div>


                    <div class="detail-box">

                        <small>
                            Status
                        </small>

                        <strong>
                            Available
                        </strong>

                    </div>

                </div>


                <button
                    class="book-btn"
                    onclick="
                        selectTrain(
                            '${trainId}'
                        )
                    ">

                    Book This Train

                </button>

            `;


            container.appendChild(
                card
            );

        }
    );
}


/* =========================================
   SEARCH
========================================= */

function initializeTrainSearch() {

    const input =
        document.getElementById(
            "trainSearch"
        );


    if (!input) {
        return;
    }


    input.addEventListener(
        "input",
        function () {

            const value =
                this.value
                    .toLowerCase()
                    .trim();


            const filtered =
                allTrains.filter(
                    train => {

                        const text = `

                            ${train.trainName || ""}

                            ${train.trainNumber || ""}

                            ${train.source || ""}

                            ${train.destination || ""}

                        `.toLowerCase();


                        return text.includes(
                            value
                        );
                    }
                );


            displayTrains(
                filtered
            );

        }
    );
}


/* =========================================
   SELECT TRAIN
========================================= */

function selectTrain(
    trainId
) {

    const train =
        allTrains.find(
            item =>
                String(
                    item.id ??
                    item.trainId
                ) === String(trainId)
        );


    if (!train) {

        alert(
            "Train not found."
        );

        return;
    }


    /*
     * Save train for booking flow.
     */

    localStorage.setItem(
        "selectedTrain",
        JSON.stringify({

            id:
                train.id ??
                train.trainId,

            trainId:
                train.id ??
                train.trainId,

            trainName:
                train.trainName ||
                train.name,

            trainNumber:
                train.trainNumber ||
                train.number,

            source:
                train.source ||
                train.fromStation,

            destination:
                train.destination ||
                train.toStation,

            departureTime:
                train.departureTime ||
                train.departure,

            arrivalTime:
                train.arrivalTime ||
                train.arrival,

            totalSeats:
            train.totalSeats,

            availableSeats:
                train.availableSeats ??
                train.seatsAvailable

        })
    );


    window.location.href =
        "booking.html";
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