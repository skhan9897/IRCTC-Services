/* =========================================
   ADMIN.JS
   Railway Booking System
========================================= */


/* =========================================
   ADMIN LOGIN CHECK
========================================= */

const userId =
    localStorage.getItem("userId");

const userRole =
    localStorage.getItem("userRole");


if (!userId) {

    window.location.href =
        "login.html";

}


/*
 * Agar role backend response mein
 * ADMIN aa raha hai tabhi admin page.
 */

if (
    userRole &&
    userRole.toUpperCase() !== "ADMIN"
) {

    alert(
        "Access denied. Admin only."
    );

    window.location.href =
        "dashboard.html";
}


/* =========================================
   API HELPER
========================================= */

async function adminFetch(
    url,
    options = {}
) {

    const response =
        await fetch(
            url,
            {
                ...options,

                headers: {
                    "Content-Type":
                        "application/json",

                    ...(options.headers || {})
                }
            }
        );


    const text =
        await response.text();


    let data;

    try {

        data =
            text
                ? JSON.parse(text)
                : null;

    } catch {

        data = text;
    }


    if (!response.ok) {

        throw new Error(
            data?.message ||
            data ||
            "Request failed"
        );
    }


    return data;
}


/* =========================================
   LOAD DASHBOARD STATISTICS
========================================= */

async function loadAdminStats() {

    try {

        const stats =
            await adminFetch(
                "/api/admin/dashboard"
            );


        setText(
            "totalUsers",
            stats.totalUsers ?? 0
        );


        setText(
            "totalTrains",
            stats.totalTrains ?? 0
        );


        setText(
            "totalStations",
            stats.totalStations ?? 0
        );


        setText(
            "totalBookings",
            stats.totalBookings ?? 0
        );


        setText(
            "totalPayments",
            stats.totalPayments ?? 0
        );


    } catch (error) {

        console.error(
            "Admin statistics:",
            error
        );

    }
}


/* =========================================
   USERS
========================================= */

async function loadUsers() {

    const container =
        document.getElementById(
            "userTableBody"
        );


    if (!container) {
        return;
    }


    container.innerHTML = `
        <tr>
            <td colspan="6">
                Loading users...
            </td>
        </tr>
    `;


    try {

        const users =
            await adminFetch(
                "/api/users"
            );


        if (
            !users ||
            users.length === 0
        ) {

            container.innerHTML = `
                <tr>
                    <td colspan="6">
                        No users found.
                    </td>
                </tr>
            `;

            return;
        }


        container.innerHTML = "";


        users.forEach(
            user => {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                    user.id ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    user.name ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    user.email ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    user.mobile ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    user.role ?? "USER"
                )}
                    </td>

                    <td>

                        <button
                            class="admin-btn danger"
                            onclick="deleteUser(
                                ${user.id}
                            )">

                            Delete

                        </button>

                    </td>

                `;


                container.appendChild(row);

            }
        );


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <tr>
                <td colspan="6">
                    Unable to load users.
                </td>
            </tr>
        `;
    }
}


/* =========================================
   DELETE USER
========================================= */

async function deleteUser(
    id
) {

    if (
        !confirm(
            "Are you sure you want to delete this user?"
        )
    ) {
        return;
    }


    try {

        await adminFetch(
            `/api/users/${id}`,
            {
                method: "DELETE"
            }
        );


        alert(
            "User deleted successfully."
        );


        loadUsers();

        loadAdminStats();


    } catch (error) {

        alert(
            "Unable to delete user: " +
            error.message
        );
    }
}


/* =========================================
   TRAINS
========================================= */

async function loadTrains() {

    const container =
        document.getElementById(
            "trainTableBody"
        );


    if (!container) {
        return;
    }


    container.innerHTML = `
        <tr>
            <td colspan="7">
                Loading trains...
            </td>
        </tr>
    `;


    try {

        const trains =
            await adminFetch(
                "/api/trains"
            );


        if (
            !trains ||
            trains.length === 0
        ) {

            container.innerHTML = `
                <tr>
                    <td colspan="7">
                        No trains found.
                    </td>
                </tr>
            `;

            return;
        }


        container.innerHTML = "";


        trains.forEach(
            train => {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                    train.id ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    train.trainNumber ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    train.trainName ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    train.source ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    train.destination ?? "-"
                )}
                    </td>

                    <td>
                        ${train.totalSeats ?? 0}
                    </td>

                    <td>

                        <button
                            class="admin-btn danger"
                            onclick="deleteTrain(
                                ${train.id}
                            )">

                            Delete

                        </button>

                    </td>

                `;


                container.appendChild(row);

            }
        );


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <tr>
                <td colspan="7">
                    Unable to load trains.
                </td>
            </tr>
        `;
    }
}


/* =========================================
   ADD TRAIN
========================================= */

async function addTrain(
    event
) {

    event.preventDefault();


    const trainNumber =
        getValue("trainNumber");

    const trainName =
        getValue("trainName");

    const source =
        getValue("source");

    const destination =
        getValue("destination");

    const totalSeats =
        Number(
            getValue("totalSeats")
        );


    if (
        !trainNumber ||
        !trainName ||
        !source ||
        !destination ||
        !totalSeats
    ) {

        alert(
            "Please fill all train details."
        );

        return;
    }


    try {

        await adminFetch(
            "/api/trains",
            {
                method: "POST",

                body: JSON.stringify({

                    trainNumber:
                    trainNumber,

                    trainName:
                    trainName,

                    source:
                    source,

                    destination:
                    destination,

                    totalSeats:
                    totalSeats

                })
            }
        );


        alert(
            "Train added successfully."
        );


        event.target.reset();


        loadTrains();

        loadAdminStats();


    } catch (error) {

        alert(
            "Unable to add train: " +
            error.message
        );
    }
}


/* =========================================
   DELETE TRAIN
========================================= */

async function deleteTrain(
    id
) {

    if (
        !confirm(
            "Delete this train?"
        )
    ) {
        return;
    }


    try {

        await adminFetch(
            `/api/trains/${id}`,
            {
                method: "DELETE"
            }
        );


        alert(
            "Train deleted successfully."
        );


        loadTrains();

        loadAdminStats();


    } catch (error) {

        alert(
            "Unable to delete train: " +
            error.message
        );
    }
}


/* =========================================
   STATIONS
========================================= */

async function loadStations() {

    const container =
        document.getElementById(
            "stationTableBody"
        );


    if (!container) {
        return;
    }


    try {

        const stations =
            await adminFetch(
                "/api/stations"
            );


        container.innerHTML = "";


        if (
            !stations ||
            stations.length === 0
        ) {

            container.innerHTML = `
                <tr>
                    <td colspan="5">
                        No stations found.
                    </td>
                </tr>
            `;

            return;
        }


        stations.forEach(
            station => {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                    station.id ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    station.stationCode ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    station.stationName ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    station.city ?? "-"
                )}
                    </td>

                    <td>

                        <button
                            class="admin-btn danger"
                            onclick="deleteStation(
                                ${station.id}
                            )">

                            Delete

                        </button>

                    </td>

                `;


                container.appendChild(row);

            }
        );


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <tr>
                <td colspan="5">
                    Unable to load stations.
                </td>
            </tr>
        `;
    }
}


/* =========================================
   ADD STATION
========================================= */

async function addStation(
    event
) {

    event.preventDefault();


    const stationCode =
        getValue("stationCode");

    const stationName =
        getValue("stationName");

    const city =
        getValue("city");


    if (
        !stationCode ||
        !stationName ||
        !city
    ) {

        alert(
            "Please fill all station details."
        );

        return;
    }


    try {

        await adminFetch(
            "/api/stations",
            {
                method: "POST",

                body: JSON.stringify({

                    stationCode:
                    stationCode,

                    stationName:
                    stationName,

                    city:
                    city

                })
            }
        );


        alert(
            "Station added successfully."
        );


        event.target.reset();

        loadStations();

        loadAdminStats();


    } catch (error) {

        alert(
            "Unable to add station: " +
            error.message
        );
    }
}


/* =========================================
   DELETE STATION
========================================= */

async function deleteStation(
    id
) {

    if (
        !confirm(
            "Delete this station?"
        )
    ) {
        return;
    }


    try {

        await adminFetch(
            `/api/stations/${id}`,
            {
                method: "DELETE"
            }
        );


        alert(
            "Station deleted successfully."
        );


        loadStations();

        loadAdminStats();


    } catch (error) {

        alert(
            "Unable to delete station: " +
            error.message
        );
    }
}


/* =========================================
   BOOKINGS
========================================= */

async function loadAllBookings() {

    const container =
        document.getElementById(
            "bookingTableBody"
        );


    if (!container) {
        return;
    }


    try {

        const bookings =
            await adminFetch(
                "/api/bookings"
            );


        container.innerHTML = "";


        if (
            !bookings ||
            bookings.length === 0
        ) {

            container.innerHTML = `
                <tr>
                    <td colspan="8">
                        No bookings found.
                    </td>
                </tr>
            `;

            return;
        }


        bookings.forEach(
            booking => {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                    booking.id ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    booking.pnr ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    booking.user?.name ??
                    booking.userName ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    booking.fromStation ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    booking.toStation ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    booking.journeyDate ??
                    "-"
                )}
                    </td>

                    <td>
                        ₹${booking.totalFare ?? 0}
                    </td>

                    <td>

                        <span class="status">
                            ${escapeHtml(
                    booking.bookingStatus ??
                    "PENDING"
                )}
                        </span>

                    </td>

                `;


                container.appendChild(row);

            }
        );


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <tr>
                <td colspan="8">
                    Unable to load bookings.
                </td>
            </tr>
        `;
    }
}


/* =========================================
   PAYMENTS
========================================= */

async function loadPayments() {

    const container =
        document.getElementById(
            "paymentTableBody"
        );


    if (!container) {
        return;
    }


    try {

        const payments =
            await adminFetch(
                "/api/payments"
            );


        container.innerHTML = "";


        if (
            !payments ||
            payments.length === 0
        ) {

            container.innerHTML = `
                <tr>
                    <td colspan="7">
                        No payments found.
                    </td>
                </tr>
            `;

            return;
        }


        payments.forEach(
            payment => {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                    payment.id ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    payment.transactionId ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    payment.bookingId ??
                    "-"
                )}
                    </td>

                    <td>
                        ₹${payment.amount ?? 0}
                    </td>

                    <td>
                        ${escapeHtml(
                    payment.paymentMethod ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    payment.paymentStatus ??
                    "-"
                )}
                    </td>

                    <td>
                        ${formatDate(
                    payment.paymentDate
                )}
                    </td>

                `;


                container.appendChild(row);

            }
        );


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <tr>
                <td colspan="7">
                    Unable to load payments.
                </td>
            </tr>
        `;
    }
}


/* =========================================
   TRAIN SCHEDULE
========================================= */

async function loadSchedules() {

    const container =
        document.getElementById(
            "scheduleTableBody"
        );


    if (!container) {
        return;
    }


    try {

        const schedules =
            await adminFetch(
                "/api/train-schedules"
            );


        container.innerHTML = "";


        if (
            !schedules ||
            schedules.length === 0
        ) {

            container.innerHTML = `
                <tr>
                    <td colspan="7">
                        No schedules found.
                    </td>
                </tr>
            `;

            return;
        }


        schedules.forEach(
            schedule => {

                const row =
                    document.createElement(
                        "tr"
                    );


                row.innerHTML = `

                    <td>
                        ${escapeHtml(
                    schedule.id ?? "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    schedule.train?.trainNumber ??
                    schedule.trainNumber ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    schedule.source ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    schedule.destination ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    schedule.departureTime ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    schedule.arrivalTime ??
                    "-"
                )}
                    </td>

                    <td>
                        ${escapeHtml(
                    schedule.journeyDate ??
                    "-"
                )}
                    </td>

                `;


                container.appendChild(row);

            }
        );


    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <tr>
                <td colspan="7">
                    Unable to load schedules.
                </td>
            </tr>
        `;
    }
}


/* =========================================
   SEARCH TABLE
========================================= */

function filterTable(
    inputId,
    tableId
) {

    const input =
        document.getElementById(
            inputId
        );


    const table =
        document.getElementById(
            tableId
        );


    if (!input || !table) {
        return;
    }


    const value =
        input.value
            .toLowerCase()
            .trim();


    const rows =
        table.querySelectorAll(
            "tbody tr"
        );


    rows.forEach(
        row => {

            const text =
                row.textContent
                    .toLowerCase();


            row.style.display =
                text.includes(value)
                    ? ""
                    : "none";

        }
    );
}


/* =========================================
   LOGOUT
========================================= */

function adminLogout() {

    localStorage.removeItem(
        "userId"
    );

    localStorage.removeItem(
        "userName"
    );

    localStorage.removeItem(
        "userEmail"
    );

    localStorage.removeItem(
        "userRole"
    );

    localStorage.removeItem(
        "selectedTrain"
    );

    localStorage.removeItem(
        "selectedBooking"
    );


    window.location.href =
        "index.html";
}


/* =========================================
   COMMON HELPERS
========================================= */

function getValue(
    id
) {

    const element =
        document.getElementById(id);


    return element
        ? element.value.trim()
        : "";
}


function setText(
    id,
    value
) {

    const element =
        document.getElementById(id);


    if (element) {

        element.textContent =
            value;

    }
}


function formatDate(
    value
) {

    if (!value) {
        return "-";
    }


    try {

        return new Date(value)
            .toLocaleString();

    } catch {

        return value;
    }
}


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
   PAGE INITIALIZATION
========================================= */

document.addEventListener(
    "DOMContentLoaded",
    function() {

        /*
         * Ye functions sirf tab
         * execute honge jab corresponding
         * table/page available hoga.
         */

        loadAdminStats();

        loadUsers();

        loadTrains();

        loadStations();

        loadAllBookings();

        loadPayments();

        loadSchedules();

    }
);