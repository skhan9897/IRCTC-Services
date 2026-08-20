/* =========================================
   API.JS
   Railway Booking System
   Common API Utility
========================================= */

const API = {

    BASE_URL: "",


    /* =====================================
       COMMON REQUEST
    ===================================== */

    async request(
        endpoint,
        options = {}
    ) {

        const config = {
            method: options.method || "GET",

            headers: {
                "Content-Type":
                    "application/json",

                ...(options.headers || {})
            }
        };


        if (
            options.body !== undefined &&
            options.body !== null
        ) {

            config.body =
                typeof options.body === "string"
                    ? options.body
                    : JSON.stringify(
                        options.body
                    );
        }


        const response =
            await fetch(
                this.BASE_URL + endpoint,
                config
            );


        const text =
            await response.text();


        let data = null;


        if (text) {

            try {

                data =
                    JSON.parse(text);

            } catch {

                data = text;
            }
        }


        if (!response.ok) {

            throw new Error(
                data?.message ||
                data?.error ||
                data ||
                `Request failed (${response.status})`
            );
        }


        return data;
    },


    /* =====================================
       AUTH
    ===================================== */

    async login(
        email,
        password
    ) {

        return this.request(
            "/api/auth/login",
            {
                method: "POST",

                body: {
                    email: email,
                    password: password
                }
            }
        );
    },


    async register(
        name,
        email,
        mobile,
        password
    ) {

        return this.request(
            "/api/auth/register",
            {
                method: "POST",

                body: {
                    name: name,
                    email: email,
                    mobile: mobile,
                    password: password
                }
            }
        );
    },


    /* =====================================
       USER
    ===================================== */

    async getUser(
        userId
    ) {

        return this.request(
            `/api/users/${userId}`
        );
    },


    async getUsers() {

        return this.request(
            "/api/users"
        );
    },


    async updateUser(
        userId,
        userData
    ) {

        return this.request(
            `/api/users/${userId}`,
            {
                method: "PUT",

                body: userData
            }
        );
    },


    async deleteUser(
        userId
    ) {

        return this.request(
            `/api/users/${userId}`,
            {
                method: "DELETE"
            }
        );
    },


    /* =====================================
       TRAIN
    ===================================== */

    async getTrains() {

        return this.request(
            "/api/trains"
        );
    },


    async getTrain(
        trainId
    ) {

        return this.request(
            `/api/trains/${trainId}`
        );
    },


    async searchTrains(
        from,
        to
    ) {

        const params =
            new URLSearchParams({

                from: from,

                to: to

            });


        return this.request(
            `/api/trains/search?${params.toString()}`
        );
    },


    async createTrain(
        train
    ) {

        return this.request(
            "/api/trains",
            {
                method: "POST",

                body: train
            }
        );
    },


    async updateTrain(
        trainId,
        train
    ) {

        return this.request(
            `/api/trains/${trainId}`,
            {
                method: "PUT",

                body: train
            }
        );
    },


    async deleteTrain(
        trainId
    ) {

        return this.request(
            `/api/trains/${trainId}`,
            {
                method: "DELETE"
            }
        );
    },


    /* =====================================
       STATION
    ===================================== */

    async getStations() {

        return this.request(
            "/api/stations"
        );
    },


    async getStation(
        stationId
    ) {

        return this.request(
            `/api/stations/${stationId}`
        );
    },


    async createStation(
        station
    ) {

        return this.request(
            "/api/stations",
            {
                method: "POST",

                body: station
            }
        );
    },


    async updateStation(
        stationId,
        station
    ) {

        return this.request(
            `/api/stations/${stationId}`,
            {
                method: "PUT",

                body: station
            }
        );
    },


    async deleteStation(
        stationId
    ) {

        return this.request(
            `/api/stations/${stationId}`,
            {
                method: "DELETE"
            }
        );
    },


    /* =====================================
       TRAIN SCHEDULE
    ===================================== */

    async getSchedules() {

        return this.request(
            "/api/train-schedules"
        );
    },


    async getSchedule(
        scheduleId
    ) {

        return this.request(
            `/api/train-schedules/${scheduleId}`
        );
    },


    async searchSchedules(
        from,
        to,
        journeyDate
    ) {

        const params =
            new URLSearchParams({

                from: from,

                to: to,

                journeyDate:
                journeyDate

            });


        return this.request(
            `/api/train-schedules/search?${params.toString()}`
        );
    },


    async createSchedule(
        schedule
    ) {

        return this.request(
            "/api/train-schedules",
            {
                method: "POST",

                body: schedule
            }
        );
    },


    async updateSchedule(
        scheduleId,
        schedule
    ) {

        return this.request(
            `/api/train-schedules/${scheduleId}`,
            {
                method: "PUT",

                body: schedule
            }
        );
    },


    async deleteSchedule(
        scheduleId
    ) {

        return this.request(
            `/api/train-schedules/${scheduleId}`,
            {
                method: "DELETE"
            }
        );
    },


    /* =====================================
       BOOKING
    ===================================== */

    async getBookings() {

        return this.request(
            "/api/bookings"
        );
    },


    async getBooking(
        bookingId
    ) {

        return this.request(
            `/api/bookings/${bookingId}`
        );
    },


    async getUserBookings(
        userId
    ) {

        return this.request(
            `/api/bookings/user/${userId}`
        );
    },


    async createBooking(
        booking
    ) {

        return this.request(
            "/api/bookings",
            {
                method: "POST",

                body: booking
            }
        );
    },


    async cancelBooking(
        bookingId
    ) {

        return this.request(
            `/api/bookings/${bookingId}/cancel`,
            {
                method: "POST"
            }
        );
    },


    async updateBooking(
        bookingId,
        booking
    ) {

        return this.request(
            `/api/bookings/${bookingId}`,
            {
                method: "PUT",

                body: booking
            }
        );
    },


    /* =====================================
       PASSENGER
    ===================================== */

    async getPassengers(
        bookingId
    ) {

        return this.request(
            `/api/bookings/${bookingId}/passengers`
        );
    },


    async createPassenger(
        bookingId,
        passenger
    ) {

        return this.request(
            `/api/bookings/${bookingId}/passengers`,
            {
                method: "POST",

                body: passenger
            }
        );
    },


    async updatePassenger(
        passengerId,
        passenger
    ) {

        return this.request(
            `/api/passengers/${passengerId}`,
            {
                method: "PUT",

                body: passenger
            }
        );
    },


    async deletePassenger(
        passengerId
    ) {

        return this.request(
            `/api/passengers/${passengerId}`,
            {
                method: "DELETE"
            }
        );
    },


    /* =====================================
       PAYMENT
    ===================================== */

    async getPayments() {

        return this.request(
            "/api/payments"
        );
    },


    async getPayment(
        paymentId
    ) {

        return this.request(
            `/api/payments/${paymentId}`
        );
    },


    async createPayment(
        payment
    ) {

        return this.request(
            "/api/payments",
            {
                method: "POST",

                body: payment
            }
        );
    },


    async getBookingPayment(
        bookingId
    ) {

        return this.request(
            `/api/payments/booking/${bookingId}`
        );
    },


    /* =====================================
       ADMIN
    ===================================== */

    async getAdminDashboard() {

        return this.request(
            "/api/admin/dashboard"
        );
    }

};


/* =========================================
   LOCAL STORAGE
========================================= */

const Auth = {

    saveLogin(data) {

        if (data.userId !== undefined) {

            localStorage.setItem(
                "userId",
                data.userId
            );
        }


        if (data.name) {

            localStorage.setItem(
                "userName",
                data.name
            );
        }


        if (data.email) {

            localStorage.setItem(
                "userEmail",
                data.email
            );
        }


        if (data.role) {

            localStorage.setItem(
                "userRole",
                data.role
            );
        }
    },


    getUserId() {

        return localStorage.getItem(
            "userId"
        );
    },


    getRole() {

        return localStorage.getItem(
            "userRole"
        );
    },


    isLoggedIn() {

        return !!localStorage.getItem(
            "userId"
        );
    },


    isAdmin() {

        return (
            this.getRole() || ""
        ).toUpperCase() === "ADMIN";
    },


    logout() {

        localStorage.clear();

        window.location.href =
            "index.html";
    }

};


/* =========================================
   COMMON HELPERS
========================================= */

function showApiError(
    error,
    elementId = null
) {

    console.error(error);


    if (elementId) {

        const element =
            document.getElementById(
                elementId
            );


        if (element) {

            element.textContent =
                error.message ||
                "Something went wrong.";

            return;
        }
    }


    alert(
        error.message ||
        "Something went wrong."
    );
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