/* =========================================
   PAYMENT.JS
   Railway Booking System
========================================= */

let booking = null;
let selectedPaymentMethod = "";


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

        loadBooking();

        initializePaymentForm();

        initializePaymentMethods();
    }
);


/* =========================================
   LOAD BOOKING
========================================= */

function loadBooking() {

    const savedBooking =
        localStorage.getItem("booking");


    if (!savedBooking) {

        alert(
            "Booking information not found."
        );

        window.location.href =
            "search-train.html";

        return;
    }


    try {

        booking =
            JSON.parse(savedBooking);

    } catch (error) {

        console.error(error);

        alert(
            "Invalid booking information."
        );

        window.location.href =
            "dashboard.html";

        return;
    }


    displayBooking();
}


/* =========================================
   DISPLAY BOOKING
========================================= */

function displayBooking() {

    const train =
        booking.train || {};


    setText(
        "pnr",
        booking.pnr || "Pending"
    );


    setText(
        "trainName",
        train.trainName ||
        booking.trainName ||
        "Train"
    );


    setText(
        "trainNumber",
        train.trainNumber ||
        booking.trainNumber ||
        "-"
    );


    setText(
        "fromStation",
        booking.fromStation ||
        train.source ||
        "-"
    );


    setText(
        "toStation",
        booking.toStation ||
        train.destination ||
        "-"
    );


    setText(
        "journeyDate",
        booking.journeyDate ||
        localStorage.getItem(
            "journeyDate"
        ) ||
        "-"
    );


    setText(
        "classType",
        booking.classType ||
        localStorage.getItem(
            "classType"
        ) ||
        "-"
    );


    const passengerList =
        booking.passengers ||
        JSON.parse(
            localStorage.getItem(
                "passengers"
            ) || "[]"
        );


    setText(
        "passengerCount",
        passengerList.length
    );


    const totalFare =
        Number(
            booking.totalFare ??
            localStorage.getItem(
                "bookingFare"
            ) ??
            0
        );


    setText(
        "totalFare",
        totalFare
    );


    localStorage.setItem(
        "bookingFare",
        totalFare
    );
}


/* =========================================
   PAYMENT METHODS
========================================= */

function initializePaymentMethods() {

    const methods =
        document.querySelectorAll(
            "input[name='paymentMethod']"
        );


    methods.forEach(
        method => {

            method.addEventListener(
                "change",
                function () {

                    selectedPaymentMethod =
                        this.value;


                    updatePaymentUI();

                }
            );

        }
    );
}


/* =========================================
   PAYMENT UI
========================================= */

function updatePaymentUI() {

    const sections =
        document.querySelectorAll(
            ".payment-details"
        );


    sections.forEach(
        section => {

            section.style.display =
                "none";

        }
    );


    const selectedSection =
        document.getElementById(
            selectedPaymentMethod +
            "Details"
        );


    if (selectedSection) {

        selectedSection.style.display =
            "block";
    }
}


/* =========================================
   PAYMENT FORM
========================================= */

function initializePaymentForm() {

    const form =
        document.getElementById(
            "paymentForm"
        );


    if (!form) {
        return;
    }


    form.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();


            await processPayment();

        }
    );
}


/* =========================================
   PROCESS PAYMENT
========================================= */

async function processPayment() {

    if (!booking) {

        showPaymentMessage(
            "Booking information not found.",
            "error"
        );

        return;
    }


    if (!selectedPaymentMethod) {

        /*
         * In case radio buttons are not
         * available, try common select.
         */

        const methodSelect =
            document.getElementById(
                "paymentMethod"
            );


        if (methodSelect) {

            selectedPaymentMethod =
                methodSelect.value;
        }
    }


    if (!selectedPaymentMethod) {

        showPaymentMessage(
            "Please select a payment method.",
            "error"
        );

        return;
    }


    const validation =
        validatePaymentDetails();


    if (!validation.valid) {

        showPaymentMessage(
            validation.message,
            "error"
        );

        return;
    }


    const button =
        document.getElementById(
            "payButton"
        );


    try {

        if (button) {

            button.disabled =
                true;

            button.textContent =
                "Processing Payment...";
        }


        const amount =
            Number(
                booking.totalFare ??
                localStorage.getItem(
                    "bookingFare"
                ) ??
                0
            );


        /*
         * Payment request
         */

        const paymentData = {

            bookingId:
                booking.id ||
                Number(
                    localStorage.getItem(
                        "bookingId"
                    )
                ),

            userId:
                Number(
                    Auth.getUserId()
                ),

            amount:
            amount,

            paymentMethod:
            selectedPaymentMethod,

            paymentStatus:
                "SUCCESS"

        };


        /*
         * IMPORTANT:
         *
         * Real payment gateway integration
         * should verify payment on backend.
         */

        const payment =
            await API.createPayment(
                paymentData
            );


        /*
         * Save payment information
         */

        localStorage.setItem(
            "payment",
            JSON.stringify(
                payment
            )
        );


        /*
         * Save updated booking
         */

        if (
            payment.booking
        ) {

            booking =
                payment.booking;

        }


        localStorage.setItem(
            "selectedBooking",
            JSON.stringify(
                booking
            )
        );


        showPaymentMessage(
            "Payment successful! Redirecting to ticket...",
            "success"
        );


        setTimeout(
            function () {

                window.location.href =
                    "ticket.html";

            },
            1000
        );


    } catch (error) {

        console.error(
            "Payment error:",
            error
        );


        showPaymentMessage(
            error.message ||
            "Payment failed.",
            "error"
        );


        if (button) {

            button.disabled =
                false;

            button.textContent =
                "Pay Now";
        }
    }
}


/* =========================================
   VALIDATE PAYMENT
========================================= */

function validatePaymentDetails() {

    const method =
        selectedPaymentMethod
            .toUpperCase();


    /* ==============================
       UPI
    ============================== */

    if (
        method === "UPI"
    ) {

        const upi =
            getValue("upiId");


        if (!upi) {

            return {
                valid: false,

                message:
                    "Please enter your UPI ID."
            };
        }


        if (
            !/^[\w.-]+@[\w.-]+$/
                .test(upi)
        ) {

            return {
                valid: false,

                message:
                    "Please enter a valid UPI ID."
            };
        }
    }


    /* ==============================
       CARD
    ============================== */

    if (
        method === "CARD" ||
        method === "CREDIT_CARD" ||
        method === "DEBIT_CARD"
    ) {

        const cardNumber =
            getValue("cardNumber")
                .replace(/\s/g, "");


        const cardName =
            getValue("cardName");


        const expiry =
            getValue("expiry");


        const cvv =
            getValue("cvv");


        if (
            !/^\d{16}$/.test(
                cardNumber
            )
        ) {

            return {
                valid: false,

                message:
                    "Please enter a valid 16 digit card number."
            };
        }


        if (!cardName) {

            return {
                valid: false,

                message:
                    "Please enter card holder name."
            };
        }


        if (
            !/^\d{2}\/\d{2}$/.test(
                expiry
            )
        ) {

            return {
                valid: false,

                message:
                    "Expiry must be in MM/YY format."
            };
        }


        if (
            !/^\d{3,4}$/.test(
                cvv
            )
        ) {

            return {
                valid: false,

                message:
                    "Please enter a valid CVV."
            };
        }
    }


    /* ==============================
       NET BANKING
    ============================== */

    if (
        method === "NET_BANKING"
    ) {

        const bank =
            getValue("bank");


        if (!bank) {

            return {
                valid: false,

                message:
                    "Please select your bank."
            };
        }
    }


    return {
        valid: true,

        message: ""
    };
}


/* =========================================
   PAYMENT MESSAGE
========================================= */

function showPaymentMessage(
    message,
    type
) {

    const element =
        document.getElementById(
            "paymentMessage"
        );


    if (!element) {

        alert(message);

        return;
    }


    element.textContent =
        message;


    element.className =
        "payment-message " +
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
            value ?? "-";
    }
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