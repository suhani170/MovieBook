
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String error = request.getParameter("error");
    String context = request.getContextPath();

    if (session.getAttribute("adminUsername") != null) {
        response.sendRedirect(context + "/admin-dashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login | MovieBook</title>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html {
            scroll-behavior: smooth;
        }

        body {
            min-height: 100vh;
            background: #090810;
            color: #fff;
            font-family: 'Inter', sans-serif;
        }

        a {
            text-decoration: none;
            color: inherit;
        }

        /* NAVBAR */

        .navbar {
            min-height: 76px;
            padding: 12px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: rgba(15, 13, 26, .98);
            border-bottom: 1px solid #272137;
            position: sticky;
            top: 0;
            z-index: 100;
            gap: 20px;
        }

        .logo {
            font-size: 27px;
            font-weight: 800;
            color: #a78bfa;
            letter-spacing: -1px;
            white-space: nowrap;
        }

        .nav-links {
            display: flex;
            align-items: center;
            gap: 25px;
            flex-wrap: wrap;
            font-size: 14px;
            color: #d0cbdc;
        }

        .nav-links a {
            transition: .25s;
        }

        .nav-links a:hover {
            color: #a78bfa;
        }

        .nav-links .active {
            color: #a78bfa;
            font-weight: 800;
        }

        .nav-btn {
            padding: 11px 19px;
            border-radius: 9px;
            background: #8254ee;
            color: white !important;
            font-weight: 700;
        }

        .nav-btn:hover {
            background: #a78bfa;
        }

        .admin-btn {
            padding: 11px 17px;
            border: 1px solid #9568f5;
            border-radius: 9px;
            background: rgba(130, 84, 238, .17);
            color: #c7adff !important;
            font-weight: 800;
            white-space: nowrap;
        }

        /* PAGE */

        .page-content {
            min-height: calc(100vh - 76px);
            padding: 65px 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            background:
                radial-gradient(ellipse at 15% 20%, #44246c 0, transparent 35%),
                radial-gradient(ellipse at 85% 80%, #392064 0, transparent 35%),
                #090810;
        }

        .login-card {
            width: 100%;
            max-width: 440px;
            padding: 42px;
            border: 1px solid #493365;
            border-radius: 18px;
            background: #171421;
            box-shadow: 0 25px 80px #0009;
        }

        .movie-icon {
            text-align: center;
            font-size: 42px;
            margin-bottom: 12px;
        }

        .brand {
            text-align: center;
            font-size: 27px;
            font-weight: 900;
            color: #b18aff;
            margin-bottom: 28px;
        }

        .brand span {
            color: white;
        }

        h1 {
            text-align: center;
            font-size: 26px;
            margin-bottom: 9px;
        }

        .subtitle {
            text-align: center;
            color: #aaa1b9;
            font-size: 13px;
            line-height: 1.7;
            margin-bottom: 30px;
        }

        .error {
            padding: 13px;
            margin-bottom: 20px;
            border: 1px solid #9e3c58;
            border-radius: 9px;
            background: #451d2d;
            color: #ffc4d1;
            font-size: 13px;
            text-align: center;
        }

        .field {
            margin-bottom: 20px;
        }

        label {
            display: block;
            color: #ddd4e9;
            font-size: 13px;
            font-weight: 700;
            margin-bottom: 9px;
        }

        input {
            width: 100%;
            padding: 15px;
            border: 1px solid #493b5e;
            border-radius: 10px;
            outline: none;
            background: #100d19;
            color: white;
            font-size: 14px;
        }

        input:focus {
            border-color: #a77aff;
            box-shadow: 0 0 0 3px #8956ef25;
        }

        input::placeholder {
            color: #766d83;
        }

        .password-wrap {
            position: relative;
        }

        .password-wrap input {
            padding-right: 75px;
        }

        .show-password {
            position: absolute;
            right: 13px;
            top: 50%;
            transform: translateY(-50%);
            border: 0;
            background: transparent;
            color: #b18aff;
            cursor: pointer;
            font-size: 12px;
            font-weight: 700;
        }

        .login-btn {
            width: 100%;
            padding: 15px;
            margin-top: 7px;
            border: 0;
            border-radius: 10px;
            background: linear-gradient(135deg, #8956ef, #a77aff);
            color: white;
            font-size: 15px;
            font-weight: 800;
            cursor: pointer;
            transition: .25s;
        }

        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 12px 28px #8956ef55;
        }

        .back {
            display: block;
            text-align: center;
            margin-top: 25px;
            color: #b9afc9;
            font-size: 13px;
        }

        .back:hover {
            color: #a78bfa;
        }

        .security {
            margin-top: 28px;
            padding-top: 20px;
            border-top: 1px solid #352943;
            text-align: center;
            color: #82788f;
            font-size: 11px;
        }

        /* FOOTER */

        footer {
            padding: 25px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            color: #81798d;
            background: #090810;
            border-top: 1px solid #292337;
            font-size: 13px;
        }

        .footer-links {
            display: flex;
            align-items: center;
            gap: 20px;
            flex-wrap: wrap;
        }

        .footer-links a:hover {
            color: #a78bfa;
        }

        @media(max-width: 700px) {
            .navbar {
                padding: 20px 5%;
                flex-direction: column;
                gap: 18px;
            }

            .nav-links {
                justify-content: center;
                gap: 15px;
            }

            .page-content {
                padding: 40px 16px;
            }

            footer {
                flex-direction: column;
                text-align: center;
            }

            .footer-links {
                justify-content: center;
            }
        }

        @media(max-width: 480px) {
            .login-card {
                padding: 30px 23px;
            }
        }
    </style>
</head>

<body>

    <!-- NAVBAR -->

    <nav class="navbar">

        <a href="<%= context %>/index.jsp" class="logo">MovieBook.</a>

        <div class="nav-links">

            <a href="<%= context %>/index.jsp">Home</a>

            <a href="<%= context %>/index.jsp#movies">Movies</a>

            <a href="<%= context %>/index.jsp#features">About</a>

            <a href="<%= context %>/customer-login.jsp">Customer Login</a>

            <a href="<%= context %>/register.jsp" class="nav-btn">Register</a>

            <a href="<%= context %>/admin-login.jsp" class="admin-btn active">
                Admin Login
            </a>

        </div>

    </nav>

    <!-- ADMIN LOGIN -->

    <main class="page-content">

        <div class="login-card">

            <div class="movie-icon">🎬</div>

            <div class="brand">MovieBook<span>.</span></div>

            <h1>Admin Login</h1>

            <p class="subtitle">
                Sign in to access the MovieBook administration panel.
            </p>

            <% if ("1".equals(error)) { %>
                <div class="error">
                    Invalid username or password. Please try again.
                </div>
            <% } else if ("2".equals(error)) { %>
                <div class="error">
                    Please enter both username and password.
                </div>
            <% } else if ("3".equals(error)) { %>
                <div class="error">
                    Login could not be completed. Please try again later.
                </div>
            <% } %>

            <form action="<%= context %>/AdminLoginServlet"
                  method="post"
                  autocomplete="off"
                  id="adminLoginForm">

                <div class="field">
                    <label for="username">Admin Username</label>

                    <input
                        type="text"
                        id="username"
                        name="username"
                        placeholder="Enter your username"
                        autocomplete="off"
                        maxlength="100"
                        required>
                </div>

                <div class="field">

                    <label for="password">Password</label>

                    <div class="password-wrap">

                        <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Enter your password"
                            autocomplete="new-password"
                            readonly
                            onfocus="this.removeAttribute('readonly')"
                            required>

                        <button
                            type="button"
                            class="show-password"
                            onclick="togglePassword()">
                            Show
                        </button>

                    </div>
                </div>

                <button type="submit" class="login-btn">
                    Login to Admin Panel →
                </button>

            </form>

            <a href="<%= context %>/index.jsp" class="back">
                ← Back to MovieBook Homepage
            </a>

            <div class="security">
                🔒 Restricted access — Authorized administrators only
            </div>

        </div>

    </main>

    <!-- FOOTER -->

    <footer>

        <div>
            MovieBook | Your movie experience, reimagined.
        </div>

        <div class="footer-links">

            <a href="<%= context %>/index.jsp">Home</a>

            <a href="<%= context %>/index.jsp#movies">Movies</a>

            <a href="<%= context %>/customer-login.jsp">Customer Login</a>

            <a href="<%= context %>/admin-login.jsp">Admin Login</a>

        </div>

    </footer>

    <script>
        function togglePassword() {

            var password = document.getElementById("password");
            var button = document.querySelector(".show-password");

            if (password.type === "password") {
                password.type = "text";
                button.textContent = "Hide";
            } else {
                password.type = "password";
                button.textContent = "Show";
            }
        }

        // Clear password whenever the login page is opened or restored.
        function clearAdminPassword() {
            var password = document.getElementById("password");
            var button = document.querySelector(".show-password");

            password.value = "";
            password.type = "password";
            password.setAttribute("readonly", "readonly");

            button.textContent = "Show";
        }

        window.addEventListener("pageshow", clearAdminPassword);

        document.getElementById("adminLoginForm").addEventListener("submit", function () {
            document.getElementById("password").removeAttribute("readonly");
        });
    </script>

</body>
</html>
