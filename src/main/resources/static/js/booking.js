/* =========================================
   BOOKING.JS
   Railway Booking System
========================================= */


/* =========================================
   GLOBAL DATA
========================================= */

let selectedTrain = null;

let passengers = [];

let selectedClass =
    localStorage.getItem("classType") || "";

let selectedFare =
    Number(
        localStorage.getItem("selectedFare") || 0
    );


/* =========================================
   INITIALIZE
========================================= */

document.addEventListener(
    "DOMContentLoaded",
    function () {

        if (!Auth.isLoggedIn()) {

            window.location.href =
                "login.html";

            return;
        }


        loadSelectedTrain();

        loadPassengers();

        initializeBookingForm();

        calculateFare();

    }
);


/* =========================================
   LOAD SELECTED TRAIN
========================================= */

function loadSelectedTrain() {

    const savedTrain =
        localStorage.getItem(
            "selectedTrain"
        );


    if (!savedTrain) {

        alert(
            "Please select a train first."
        );

        window.location.href =
            "search-train.html";

        return;
    }


    try {

        selectedTrain =
            JSON.parse(savedTrain);

    } catch (error) {

        console.error(error);

        alert(
            "Invalid train information."
        );

        window.location.href =
            "search-train.html";

        return;
    }


    displayTrainDetails();
}


/* =========================================
   DISPLAY TRAIN
========================================= */

function displayTrainDetails() {

    if (!selectedTrain) {
        return;
    }


    setText(
        "trainName",
        selectedTrain.trainName || "Train"
    );


    setText(
        "trainNumber",
        selectedTrain.trainNumber || "-"
    );


    setText(
        "fromStation",
        selectedTrain.source || "-"
    );


    setText(
        "toStation",
        selectedTrain.destination || "-"
    );


    setText(
        "departureTime",
        selectedTrain.departureTime || "-"
    );


    setText(
        "arrivalTime",
        selectedTrain.arrivalTime || "-"
    );


    setText(
        "journeyDate",
        localStorage.getItem(
            "journeyDate"
        ) || "-"
    );


    setText(
        "classType",
        selectedClass || "-"
    );


    setText(
        "farePerPassenger",
        selectedFare
    );
}


/* =========================================
   LOAD PASSENGERS
========================================= */

function loadPassengers() {

    const saved =
        localStorage.getItem(
            "passengers"
        );


    if (!saved) {

        passengers = [];

        addPassenger();

        return;
    }


    try {

        passengers =
            JSON.parse(saved);

    } catch {

        passengers = [];

    }


    if (
        !Array.isArray(passengers)
    ) {

        passengers = [];

    }


    if (
        passengers.length === 0
    ) {

        addPassenger();

    } else {

        renderPassengers();

    }
}


/* =========================================
   ADD PASSENGER
========================================= */

function addPassenger() {

    if (
        passengers.length >= 6
    ) {

        alert(
            "Maximum 6 passengers can be added."
        );

        return;
    }


    passengers.push({

        name: "",

        age: "",

        gender: ""

    });


    savePassengers();

    renderPassengers();

    calculateFare();
}


/* =========================================
   REMOVE PASSENGER
========================================= */

function removePassenger(
    index
) {

    if (
        passengers.length <= 1
    ) {

        alert(
            "At least one passenger is required."
        );

        return;
    }


    passengers.splice(
        index,
        1
    );


    savePassengers();

    renderPassengers();

    calculateFare();
}


/* =========================================
   RENDER PASSENGERS
========================================= */

function renderPassengers() {

    const container =
        document.getElementById(
            "passengersContainer"
        );


    if (!container) {
        return;
    }


    container.innerHTML = "";


    passengers.forEach(
        (passenger, index) => {

            const card =
                document.createElement(
                    "div"
                );


            card.className =
                "passenger";


            card.innerHTML = `

                <div class="passenger-header">

                    <h3>
                        Passenger ${index + 1}
                    </h3>

                    ${
                passengers.length > 1
                    ? `
                        <button
                            type="button"
                            class="remove-btn"
                            onclick="removePassenger(
                                ${index}
                            )">

                            Remove

                        </button>
                        `
                    : ""
            }

                </div>


                <div class="form-row">


                    <div class="form-group">

                        <label>
                            Full Name
                        </label>

                        <input
                            type="text"
                            value="${escapeHtml(
                passenger.name
            )}"
                            placeholder="Passenger name"
                            oninput="updatePassenger(
                                ${index},
                                'name',
                                this.value
                            )"
                            required>

                    </div>


                    <div class="form-group">

                        <label>
                            Age
                        </label>

                        <input
                            type="number"
                            min="1"
                            max="120"
                            value="${escapeHtml(
                passenger.age
            )}"
                            placeholder="Age"
                            oninput="updatePassenger(
                                ${index},
                                'age',
                                this.value
                            )"
                            required>

                    </div>


                </div>


                <div class="form-group">

                    <label>
                        Gender
                    </label>

                    <select
                        onchange="updatePassenger(
                            ${index},
                            'gender',
                            this.value
                        )">

                        <option value="">
                            Select Gender
                        </option>

                        <option
                            value="MALE"
                            ${
                passenger.gender ===
                "MALE"
                    ? "selected"
                    : ""
            }>

                            Male

                        </option>

                        <option
                            value="FEMALE"
                            ${
                passenger.gender ===
                "FEMALE"
                    ? "selected"
                    : ""
            }>

                            Female

                        </option>

                        <option
                            value="OTHER"
                            ${
                passenger.gender ===
                "OTHER"
                    ? "selected"
                    : ""
            }>

                            Other

                        </option>

                    </select>

                </div>

            `;


            container.appendChild(
                card
            );

        }
    );
}


/* =========================================
   UPDATE PASSENGER
========================================= */

function updatePassenger(
    index,
    field,
    value
) {

    if (
        !passengers[index]
    ) {
        return;
    }


    passengers[index][field] =
        value;


    savePassengers();

    calculateFare();
}


/* =========================================
   SAVE PASSENGERS
========================================= */

function savePassengers() {

    localStorage.setItem(
        "passengers",
        JSON.stringify(
            passengers
        )
    );
}


/* =========================================
   VALIDATE PASSENGERS
========================================= */

function validatePassengers() {

    if (
        passengers.length === 0
    ) {

        return {
            valid: false,
            message:
                "Please add at least one passenger."
        };
    }


    for (
        let i = 0;
        i < passengers.length;
        i++
    ) {

        const passenger =
            passengers[i];


        if (
            !passenger.name ||
            !passenger.name.trim()
        ) {

            return {
                valid: false,
                message:
                    `Please enter name for Passenger ${i + 1}.`
            };
        }


        if (
            !passenger.age ||
            Number(passenger.age) < 1 ||
            Number(passenger.age) > 120
        ) {

            return {
                valid: false,
                message:
                    `Please enter valid age for Passenger ${i + 1}.`
            };
        }


        if (
            !passenger.gender
        ) {

            return {
                valid: false,
                message:
                    `Please select gender for Passenger ${i + 1}.`
            };
        }

    }


    return {
        valid: true,
        message: ""
    };
}


/* =========================================
   FARE CALCULATION
========================================= */

function calculateFare() {

    const passengerCount =
        passengers.length;


    const baseFare =
        selectedFare *
        passengerCount;


    /*
     * Convenience/booking charge.
     * Backend should calculate final
     * amount again for security.
     */

    const bookingCharge =
        passengerCount > 0
            ? 20
            : 0;


    const totalFare =
        baseFare +
        bookingCharge;


    setText(
        "passengerCount",
        passengerCount
    );


    setText(
        "farePerPassenger",
        selectedFare
    );


    setText(
        "baseFare",
        baseFare
    );


    setText(
        "bookingCharge",
        bookingCharge
    );


    setText(
        "totalFare",
        totalFare
    );


    /*
     * Save total for payment page.
     */

    localStorage.setItem(
        "bookingFare",
        totalFare
    );
}


/* =========================================
   BOOKING FORM
========================================= */

function initializeBookingForm() {

    const form =
        document.getElementById(
            "bookingForm"
        );


    if (!form) {
        return;
    }


    form.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const validation =
                validatePassengers();


            if (!validation.valid) {

                showBookingMessage(
                    validation.message,
                    "error"
                );

                return;
            }


            await createBooking();

        }
    );
}


/* =========================================
   CREATE BOOKING
========================================= */

async function createBooking() {

    const button =
        document.getElementById(
            "continueBtn"
        );


    try {

        if (!selectedTrain) {

            throw new Error(
                "Train information not found."
            );
        }


        const validation =
            validatePassengers();


        if (!validation.valid) {

            throw new Error(
                validation.message
            );
        }


        if (button) {

            button.disabled =
                true;

            button.textContent =
                "Creating Booking...";
        }


        const userId =
            Auth.getUserId();


        const journeyDate =
            localStorage.getItem(
                "journeyDate"
            );


        /*
         * Booking request
         */

        const bookingData = {

            userId:
                Number(userId),

            trainId:
                selectedTrain.id ||
                selectedTrain.trainId,

            journeyDate:
            journeyDate,

            classType:
            selectedClass,

            passengers:
            passengers,

            totalFare:
                Number(
                    localStorage.getItem(
                        "bookingFare"
                    ) || 0
                )

        };


        /*
         * Backend booking API
         */

        const booking =
            await API.createBooking(
                bookingData
            );


        /*
         * Save booking
         */

        localStorage.setItem(
            "booking",
            JSON.stringify(
                booking
            )
        );


        localStorage.setItem(
            "bookingId",
            booking.id
        );


        localStorage.setItem(
            "bookingPnr",
            booking.pnr || ""
        );


        /*
         * Go to payment
         */

        showBookingMessage(
            "Booking created. Redirecting to payment...",
            "success"
        );


        setTimeout(
            function() {

                window.location.href =
                    "payment.html";

            },
            700
        );


    } catch (error) {

        console.error(
            "Booking error:",
            error
        );


        showBookingMessage(
            error.message ||
            "Unable to create booking.",
            "error"
        );


        if (button) {

            button.disabled =
                false;

            button.textContent =
                "Continue to Payment";
        }
    }
}


/* =========================================
   BOOKING MESSAGE
========================================= */

function showBookingMessage(
    message,
    type
) {

    const element =
        document.getElementById(
            "bookingMessage"
        );


    if (!element) {

        alert(message);

        return;
    }


    element.textContent =
        message;


    element.className =
        "booking-message " +
        type;
}


/* =========================================
   SET TEXT
========================================= */

function setText(
    id,
    value
) {

    const element =
        document.getElementById(
            id
        );


    if (element) {

        element.textContent =
            value ??
            "-";
    }
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


/* =========================================
   CLEAR BOOKING
========================================= */

function clearBookingData() {

    localStorage.removeItem(
        "selectedTrain"
    );

    localStorage.removeItem(
        "classType"
    );

    localStorage.removeItem(
        "selectedFare"
    );

    localStorage.removeItem(
        "journeyDate"
    );

    localStorage.removeItem(
        "passengers"
    );

    localStorage.removeItem(
        "bookingFare"
    );

}


/* =========================================
   BACK TO SEARCH
========================================= */

function backToSearch() {

    clearBookingData();

    window.location.href =
        "search-train.html";
}