
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create Account - MovieBook</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/moviebook.css">

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap"
          rel="stylesheet">

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            background: #090810;
            color: #fff;
            font-family: 'Inter', sans-serif;
        }

        .register-container {
            min-height: 78vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 20px;
        }

        .register-card {
            width: 100%;
            max-width: 460px;
            padding: 38px;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 20px;
            box-shadow: 0 15px 50px rgba(0, 0, 0, 0.35);
        }

        .register-icon {
            width: 65px;
            height: 65px;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 18px;
            background: #2a1d40;
            font-size: 30px;
        }

        h1 {
            text-align: center;
            font-size: 27px;
            margin: 0 0 10px;
        }

        .subtitle {
            text-align: center;
            color: #aaa5b7;
            font-size: 14px;
            margin-bottom: 25px;
        }

        .error-message {
            padding: 12px;
            border-radius: 9px;
            margin-bottom: 18px;
            font-size: 13px;
            text-align: center;
            color: #ff9999;
            background: rgba(255, 60, 60, 0.1);
            border: 1px solid rgba(255, 60, 60, 0.3);
        }

        label {
            display: block;
            margin: 17px 0 8px;
            font-size: 13px;
            font-weight: 600;
            color: #ded9e9;
        }

        .input-field {
            width: 100%;
            padding: 14px;
            background: #0e0c16;
            border: 1px solid #393247;
            border-radius: 10px;
            color: #fff;
            outline: none;
            font-size: 14px;
        }

        .input-field:focus {
            border-color: #a855f7;
            box-shadow: 0 0 0 3px rgba(168, 85, 247, 0.12);
        }

        .input-hint {
            display: block;
            color: #aaa5b7;
            font-size: 11px;
            margin-top: 7px;
            line-height: 1.6;
        }

        .password-wrapper {
            position: relative;
            width: 100%;
        }

        .password-wrapper .input-field {
            padding-right: 70px;
        }

        .toggle-password {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            border: none;
            background: transparent;
            color: #c084fc;
            font-size: 12px;
            font-weight: 600;
            cursor: pointer;
        }

        .toggle-password:hover {
            color: #e9d5ff;
        }

        .password-strength {
            height: 5px;
            margin-top: 9px;
            background: #393247;
            border-radius: 5px;
            overflow: hidden;
        }

        .strength-bar {
            height: 100%;
            width: 0;
            transition: 0.2s;
            background: #ef4444;
        }

        .strength-text {
            display: block;
            margin-top: 6px;
            font-size: 11px;
            color: #aaa5b7;
        }

        .register-button {
            width: 100%;
            margin-top: 25px;
            padding: 14px;
            border: none;
            border-radius: 10px;
            background: linear-gradient(135deg, #9333ea, #6d28d9);
            color: white;
            font-size: 15px;
            font-weight: 700;
            cursor: pointer;
            transition: 0.2s;
        }

        .register-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 20px rgba(147, 51, 234, 0.3);
        }

        .login-link {
            text-align: center;
            margin-top: 25px;
            color: #aaa5b7;
            font-size: 13px;
        }

        a {
            color: #c084fc;
            text-decoration: none;
        }

        a:hover {
            color: #e9d5ff;
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 18px;
            color: #aaa5b7;
            font-size: 13px;
        }

        @media (max-width: 480px) {
            .register-card {
                padding: 27px 22px;
            }

            h1 {
                font-size: 24px;
            }
        }
    </style>
</head>

<body>

<%@ include file="includes/navbar.jspf" %>

<div class="register-container">
    <div class="register-card">

        <div class="register-icon">🎟️</div>

        <h1>Create Your Account</h1>

        <p class="subtitle">
            Join MovieBook and enjoy your favourite movies.
        </p>

        <c:if test="${not empty requestScope.error}">
            <div class="error-message">
                <c:out value="${requestScope.error}"/>
            </div>
        </c:if>

        <form id="registerForm"
              action="${pageContext.request.contextPath}/RegisterServlet"
              method="post">

            <label for="name">Full Name</label>

            <input class="input-field"
                   type="text"
                   id="name"
                   name="name"
                   required
                   minlength="2"
                   maxlength="100"
                   autocomplete="name"
                   placeholder="Enter your full name"
                   value="<c:out value='${requestScope.name}'/>">

            <small class="input-hint">
                Use letters, spaces, apostrophes or hyphens.
            </small>

            <label for="email">Email Address</label>

            <input class="input-field"
                   type="email"
                   id="email"
                   name="email"
                   required
                   maxlength="100"
                   autocomplete="email"
                   placeholder="you@example.com"
                   value="<c:out value='${requestScope.email}'/>">

            <label for="password">Password</label>

            <div class="password-wrapper">
                <input class="input-field"
                       type="password"
                       id="password"
                       name="password"
                       required
                       minlength="8"
                       maxlength="64"
                       autocomplete="new-password"
                       placeholder="Create a strong password">

                <button type="button"
                        class="toggle-password"
                        onclick="togglePassword('password', this)"
                        aria-label="Show password"
                        aria-pressed="false">
                    Show
                </button>
            </div>

            <small class="input-hint">
                At least 8 characters with uppercase, lowercase,
                a number and a special character.
            </small>

            <div class="password-strength">
                <div class="strength-bar" id="strengthBar"></div>
            </div>

            <span class="strength-text" id="strengthText">
                Password strength: Not entered
            </span>

            <label for="confirmPassword">Confirm Password</label>

            <div class="password-wrapper">
                <input class="input-field"
                       type="password"
                       id="confirmPassword"
                       name="confirmPassword"
                       required
                       minlength="8"
                       maxlength="64"
                       autocomplete="new-password"
                       placeholder="Re-enter your password">

                <button type="button"
                        class="toggle-password"
                        onclick="togglePassword('confirmPassword', this)"
                        aria-label="Show confirm password"
                        aria-pressed="false">
                    Show
                </button>
            </div>

            <button type="submit" class="register-button">
                Create Account
            </button>

        </form>

        <p class="login-link">
            Already have an account?
            <a href="${pageContext.request.contextPath}/customer-login.jsp">
                Login
            </a>
        </p>

        <a class="back-link"
           href="${pageContext.request.contextPath}/index.jsp">
            ← Back to Homepage
        </a>

    </div>
</div>

<script>
    const form = document.getElementById("registerForm");
    const nameInput = document.getElementById("name");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const confirmInput = document.getElementById("confirmPassword");
    const strengthBar = document.getElementById("strengthBar");
    const strengthText = document.getElementById("strengthText");

    function togglePassword(id, button) {
        const field = document.getElementById(id);

        if (field.type === "password") {
            field.type = "text";
            button.textContent = "Hide";
            button.setAttribute("aria-label", "Hide password");
            button.setAttribute("aria-pressed", "true");
        } else {
            field.type = "password";
            button.textContent = "Show";
            button.setAttribute("aria-label", "Show password");
            button.setAttribute("aria-pressed", "false");
        }
    }

    function isValidName(value) {
        return /^[\p{L}\p{M}]+(?:[ '\u2019-][\p{L}\p{M}]+)*$/u.test(value.trim())
            && value.trim().length >= 2
            && value.trim().length <= 100;
    }

    function isStrongPassword(value) {
        return value.length >= 8
            && value.length <= 64
            && /[A-Z]/.test(value)
            && /[a-z]/.test(value)
            && /[0-9]/.test(value)
            && /[^A-Za-z0-9]/.test(value);
    }

    passwordInput.addEventListener("input", function () {
        const password = passwordInput.value;
        let score = 0;

        if (password.length >= 8) score++;
        if (/[A-Z]/.test(password)) score++;
        if (/[a-z]/.test(password)) score++;
        if (/[0-9]/.test(password)) score++;
        if (/[^A-Za-z0-9]/.test(password)) score++;

        const widths = ["0%", "20%", "40%", "60%", "80%", "100%"];
        const labels = [
            "Not entered",
            "Very weak",
            "Weak",
            "Fair",
            "Good",
            "Strong"
        ];

        strengthBar.style.width = widths[score];
        strengthText.textContent = "Password strength: " + labels[score];

        if (score <= 2) {
            strengthBar.style.background = "#ef4444";
        } else if (score <= 3) {
            strengthBar.style.background = "#f59e0b";
        } else {
            strengthBar.style.background = "#22c55e";
        }
    });

    form.addEventListener("submit", function (event) {
        const name = nameInput.value.trim();
        const email = emailInput.value.trim();
        const password = passwordInput.value;
        const confirmPassword = confirmInput.value;

        if (!isValidName(name)) {
            event.preventDefault();
            alert("Please enter a valid name using letters, spaces, apostrophes or hyphens.");
            nameInput.focus();
            return;
        }

        if (!emailInput.validity.valid || email.length === 0) {
            event.preventDefault();
            alert("Please enter a valid email address.");
            emailInput.focus();
            return;
        }

        if (!isStrongPassword(password)) {
            event.preventDefault();
            alert("Password must contain at least 8 characters, uppercase, lowercase, a number and a special character.");
            passwordInput.focus();
            return;
        }

        if (password !== confirmPassword) {
            event.preventDefault();
            alert("Password and confirm password do not match.");
            confirmInput.focus();
        }
    });
</script>

</body>
</html>
