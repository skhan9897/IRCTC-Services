/* =========================================
   DASHBOARD.JS
   Railway Booking System
========================================= */


/* =========================================
   INITIALIZE DASHBOARD
========================================= */

document.addEventListener(
    "DOMContentLoaded",
    async function () {

        // Login check
        if (!Auth.isLoggedIn()) {

            window.location.href =
                "login.html";

            return;
        }


        // Load dashboard
        loadUserInfo();

        await loadDashboardStats();

        await loadRecentBookings();

        initializeSearchForm();

    }
);


/* =========================================
   USER INFORMATION
========================================= */

function loadUserInfo() {

    const userName =
        localStorage.getItem(
            "userName"
        );


    const userEmail =
        localStorage.getItem(
            "userEmail"
        );


    setText(
        "userName",
        userName || "User"
    );


    setText(
        "userEmail",
        userEmail || ""
    );


    setText(
        "welcomeName",
        userName || "User"
    );
}


/* =========================================
   DASHBOARD STATS
========================================= */

async function loadDashboardStats() {

    try {

        const userId =
            Auth.getUserId();


        /*
         * User bookings
         */

        const bookings =
            await API.getUserBookings(
                userId
            );


        const bookingList =
            Array.isArray(bookings)
                ? bookings
                : [];


        setText(
            "totalBookings",
            bookingList.length
        );


        /*
         * Confirmed bookings
         */

        const confirmed =
            bookingList.filter(
                booking =>
                    String(
                        booking.bookingStatus ||
                        ""
                    ).toUpperCase()
                    === "CONFIRMED"
            );


        setText(
            "confirmedBookings",
            confirmed.length
        );


        /*
         * Pending bookings
         */

        const pending =
            bookingList.filter(
                booking =>
                    String(
                        booking.bookingStatus ||
                        ""
                    ).toUpperCase()
                    === "PENDING"
            );


        setText(
            "pendingBookings",
            pending.length
        );


        /*
         * Cancelled bookings
         */

        const cancelled =
            bookingList.filter(
                booking =>
                    String(
                        booking.bookingStatus ||
                        ""
                    ).toUpperCase()
                    === "CANCELLED"
            );


        setText(
            "cancelledBookings",
            cancelled.length
        );


    } catch (error) {

        console.error(
            "Dashboard stats:",
            error
        );


        setText(
            "totalBookings",
            0
        );

        setText(
            "confirmedBookings",
            0
        );

        setText(
            "pendingBookings",
            0
        );

        setText(
            "cancelledBookings",
            0
        );
    }
}


/* =========================================
   RECENT BOOKINGS
========================================= */

async function loadRecentBookings() {

    const container =
        document.getElementById(
            "recentBookings"
        );


    if (!container) {
        return;
    }


    container.innerHTML = `
        <div class="loading">
            Loading recent bookings...
        </div>
    `;


    try {

        const userId =
            Auth.getUserId();


        const bookings =
            await API.getUserBookings(
                userId
            );


        if (
            !bookings ||
            bookings.length === 0
        ) {

            container.innerHTML = `
                <div class="empty-bookings">

                    <div style="font-size:45px;">
                        🚆
                    </div>

                    <h3>
                        No bookings yet
                    </h3>

                    <p>
                        Search for a train and
                        make your first booking.
                    </p>

                    <button
                        class="search-button"
                        onclick="
                            window.location.href =
                            'search-train.html'
                        ">

                        Search Train

                    </button>

                </div>
            `;

            return;
        }


        /*
         * Latest bookings first
         */

        const sortedBookings =
            [...bookings].sort(
                (a, b) => {

                    const dateA =
                        new Date(
                            a.createdAt || 0
                        );

                    const dateB =
                        new Date(
                            b.createdAt || 0
                        );

                    return dateB - dateA;
                }
            );


        /*
         * Show only latest 5
         */

        const recentBookings =
            sortedBookings.slice(
                0,
                5
            );


        container.innerHTML = "";


        recentBookings.forEach(
            booking => {

                container.appendChild(
                    createBookingRow(
                        booking
                    )
                );

            }
        );


    } catch (error) {

        console.error(
            "Recent bookings:",
            error
        );


        container.innerHTML = `
            <div class="empty-bookings">

                Unable to load recent bookings.

                <br><br>

                <button
                    class="search-button"
                    onclick="loadRecentBookings()">

                    Retry

                </button>

            </div>
        `;
    }
}


/* =========================================
   CREATE BOOKING ROW
========================================= */

function createBookingRow(
    booking
) {

    const row =
        document.createElement(
            "div"
        );


    row.className =
        "booking-row";


    const status =
        String(
            booking.bookingStatus ||
            "PENDING"
        ).toUpperCase();


    let statusClass =
        "pending";


    if (
        status === "CONFIRMED"
    ) {

        statusClass =
            "confirmed";

    } else if (
        status === "CANCELLED"
    ) {

        statusClass =
            "cancelled";
    }


    const train =
        booking.train || {};


    const trainName =
        train.trainName ||
        booking.trainName ||
        "Train";


    const trainNumber =
        train.trainNumber ||
        booking.trainNumber ||
        "-";


    const from =
        booking.fromStation ||
        train.source ||
        "-";


    const to =
        booking.toStation ||
        train.destination ||
        "-";


    const date =
        booking.journeyDate ||
        "-";


    row.innerHTML = `

        <div>

            <strong>
                ${escapeHtml(
        trainName
    )}
            </strong>

            <br>

            <small>
                Train No:
                ${escapeHtml(
        trainNumber
    )}
            </small>

            <br>

            <small>
                ${escapeHtml(
        from
    )}

                →

                ${escapeHtml(
        to
    )}
            </small>

            <br>

            <small>
                Journey:
                ${escapeHtml(
        date
    )}
            </small>

        </div>


        <div style="text-align:right;">

            <span
                class="status ${statusClass}">

                ${escapeHtml(
        status
    )}

            </span>

            <br><br>

            <button
                class="view-ticket-btn"
                onclick="
                    viewBooking(
                        ${booking.id}
                    )
                ">

                View Ticket

            </button>

        </div>

    `;


    return row;
}


/* =========================================
   VIEW BOOKING
========================================= */

async function viewBooking(
    bookingId
) {

    try {

        const booking =
            await API.getBooking(
                bookingId
            );


        /*
         * Save selected booking
         */

        localStorage.setItem(
            "selectedBooking",
            JSON.stringify(
                booking
            )
        );


        /*
         * Load passengers if available
         */

        try {

            const passengers =
                await API.getPassengers(
                    bookingId
                );


            localStorage.setItem(
                "passengers",
                JSON.stringify(
                    passengers || []
                )
            );

        } catch (passengerError) {

            console.warn(
                "Passenger data unavailable",
                passengerError
            );
        }


        window.location.href =
            "ticket.html";


    } catch (error) {

        console.error(
            error
        );


        alert(
            error.message ||
            "Unable to open ticket."
        );
    }
}


/* =========================================
   SEARCH FORM
========================================= */

function initializeSearchForm() {

    const form =
        document.getElementById(
            "dashboardSearchForm"
        );


    if (!form) {
        return;
    }


    form.addEventListener(
        "submit",
        function(event) {

            event.preventDefault();


            const from =
                document
                    .getElementById("from")
                    .value
                    .trim();


            const to =
                document
                    .getElementById("to")
                    .value
                    .trim();


            const journeyDate =
                document
                    .getElementById(
                        "journeyDate"
                    )
                    .value;


            if (
                !from ||
                !to ||
                !journeyDate
            ) {

                alert(
                    "Please enter From, To and Journey Date."
                );

                return;
            }


            if (
                from.toLowerCase() ===
                to.toLowerCase()
            ) {

                alert(
                    "From and To stations cannot be same."
                );

                return;
            }


            /*
             * Save search
             */

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


            /*
             * Search page
             */

            window.location.href =
                "search-train.html";

        }
    );
}


/* =========================================
   QUICK ACTIONS
========================================= */

function openSearchTrain() {

    window.location.href =
        "search-train.html";
}


function openMyBookings() {

    window.location.href =
        "my-bookings.html";
}


function openProfile() {

    window.location.href =
        "profile.html";
}


function openPayments() {

    window.location.href =
        "payment.html";
}


/* =========================================
   LOGOUT
========================================= */

function dashboardLogout() {

    Auth.logout();
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