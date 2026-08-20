/* =========================================
   AUTH.JS
   Login + Register
========================================= */


/* =========================================
   LOGIN
========================================= */

async function loginUser(
    email,
    password
) {

    if (!email || !password) {

        throw new Error(
            "Email and password are required."
        );
    }


    const data =
        await API.login(
            email,
            password
        );


    /*
     * Save user information
     * in localStorage
     */

    Auth.saveLogin(data);


    return data;
}


/* =========================================
   REGISTER
========================================= */

async function registerUser(
    name,
    email,
    mobile,
    password
) {

    if (
        !name ||
        !email ||
        !mobile ||
        !password
    ) {

        throw new Error(
            "Please fill all required fields."
        );
    }


    if (
        !/^[0-9]{10}$/.test(mobile)
    ) {

        throw new Error(
            "Please enter a valid 10 digit mobile number."
        );
    }


    if (password.length < 6) {

        throw new Error(
            "Password must contain at least 6 characters."
        );
    }


    return await API.register(
        name,
        email,
        mobile,
        password
    );
}


/* =========================================
   PASSWORD MATCH
========================================= */

function validatePasswords(
    password,
    confirmPassword
) {

    if (!password) {

        return {
            valid: false,
            message:
                "Password is required."
        };
    }


    if (password.length < 6) {

        return {
            valid: false,
            message:
                "Password must contain at least 6 characters."
        };
    }


    if (
        password !==
        confirmPassword
    ) {

        return {
            valid: false,
            message:
                "Passwords do not match."
        };
    }


    return {
        valid: true,
        message: ""
    };
}


/* =========================================
   SHOW / HIDE PASSWORD
========================================= */

function togglePassword(
    inputId,
    button
) {

    const input =
        document.getElementById(
            inputId
        );


    if (!input) {
        return;
    }


    if (
        input.type ===
        "password"
    ) {

        input.type = "text";

        if (button) {
            button.textContent =
                "Hide";
        }

    } else {

        input.type = "password";

        if (button) {
            button.textContent =
                "Show";
        }
    }
}


/* =========================================
   SHOW AUTH MESSAGE
========================================= */

function showAuthMessage(
    message,
    type = "error",
    elementId = "message"
) {

    const element =
        document.getElementById(
            elementId
        );


    if (!element) {
        alert(message);
        return;
    }


    element.textContent =
        message;


    element.className =
        "message " + type;
}


/* =========================================
   LOGIN FORM
========================================= */

function initializeLoginForm() {

    const form =
        document.getElementById(
            "loginForm"
        );


    if (!form) {
        return;
    }


    form.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const email =
                document
                    .getElementById("email")
                    .value
                    .trim();


            const password =
                document
                    .getElementById("password")
                    .value;


            const button =
                document.getElementById(
                    "loginBtn"
                );


            try {

                if (!email) {

                    throw new Error(
                        "Please enter your email."
                    );
                }


                if (!password) {

                    throw new Error(
                        "Please enter your password."
                    );
                }


                if (button) {

                    button.disabled =
                        true;

                    button.textContent =
                        "Logging in...";
                }


                await loginUser(
                    email,
                    password
                );


                showAuthMessage(
                    "Login successful. Redirecting...",
                    "success"
                );


                setTimeout(
                    function() {

                        window.location.href =
                            "dashboard.html";

                    },
                    700
                );


            } catch (error) {

                console.error(
                    "Login error:",
                    error
                );


                showAuthMessage(
                    error.message ||
                    "Login failed.",
                    "error"
                );


                if (button) {

                    button.disabled =
                        false;

                    button.textContent =
                        "Login";
                }
            }

        }
    );
}


/* =========================================
   REGISTER FORM
========================================= */

function initializeRegisterForm() {

    const form =
        document.getElementById(
            "registerForm"
        );


    if (!form) {
        return;
    }


    form.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const name =
                document
                    .getElementById("name")
                    .value
                    .trim();


            const email =
                document
                    .getElementById("email")
                    .value
                    .trim();


            const mobile =
                document
                    .getElementById("mobile")
                    .value
                    .trim();


            const password =
                document
                    .getElementById("password")
                    .value;


            const confirmPassword =
                document
                    .getElementById(
                        "confirmPassword"
                    )
                    .value;


            const button =
                document.getElementById(
                    "registerBtn"
                );


            try {

                if (!name) {

                    throw new Error(
                        "Please enter your full name."
                    );
                }


                if (!email) {

                    throw new Error(
                        "Please enter your email."
                    );
                }


                if (
                    !/^[^\s@]+@[^\s@]+\.[^\s@]+$/
                        .test(email)
                ) {

                    throw new Error(
                        "Please enter a valid email address."
                    );
                }


                if (
                    !/^[0-9]{10}$/.test(mobile)
                ) {

                    throw new Error(
                        "Please enter a valid 10 digit mobile number."
                    );
                }


                const passwordCheck =
                    validatePasswords(
                        password,
                        confirmPassword
                    );


                if (
                    !passwordCheck.valid
                ) {

                    throw new Error(
                        passwordCheck.message
                    );
                }


                if (button) {

                    button.disabled =
                        true;

                    button.textContent =
                        "Creating Account...";
                }


                await registerUser(
                    name,
                    email,
                    mobile,
                    password
                );


                showAuthMessage(
                    "Registration successful. Redirecting to login...",
                    "success"
                );


                form.reset();


                setTimeout(
                    function() {

                        window.location.href =
                            "login.html";

                    },
                    1000
                );


            } catch (error) {

                console.error(
                    "Registration error:",
                    error
                );


                showAuthMessage(
                    error.message ||
                    "Registration failed.",
                    "error"
                );


                if (button) {

                    button.disabled =
                        false;

                    button.textContent =
                        "Create Account";
                }
            }

        }
    );
}


/* =========================================
   LOGOUT
========================================= */

function logoutUser() {

    Auth.logout();
}


/* =========================================
   AUTH PAGE CHECK
========================================= */

function redirectIfLoggedIn() {

    if (
        Auth.isLoggedIn()
    ) {

        window.location.href =
            "dashboard.html";
    }
}


/* =========================================
   ADMIN CHECK
========================================= */

function requireAdmin() {

    if (!Auth.isLoggedIn()) {

        window.location.href =
            "login.html";

        return false;
    }


    if (!Auth.isAdmin()) {

        alert(
            "Access denied. Admin only."
        );

        window.location.href =
            "dashboard.html";

        return false;
    }


    return true;
}


/* =========================================
   INITIALIZE
========================================= */

document.addEventListener(
    "DOMContentLoaded",
    function() {

        initializeLoginForm();

        initializeRegisterForm();

    }
);