
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.HtmlUtil"%>
<%
    String redirect = request.getParameter("redirect");

    if (redirect == null) {
        redirect = (String) request.getAttribute("redirect");
    }

    String error = (String) request.getAttribute("error");
    String email = (String) request.getAttribute("email");

    boolean justRegistered =
            "1".equals(request.getParameter("registered"));

    String context = request.getContextPath();

    if (email == null) {
        email = "";
    }

    if (redirect == null) {
        redirect = "";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Login - MovieBook</title>

    <link rel="stylesheet"
          href="<%= context %>/css/moviebook.css">

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

        .login-container {
            min-height: 78vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 45px 20px;
        }

        .login-card {
            width: 100%;
            max-width: 440px;
            padding: 38px;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 20px;
            box-shadow: 0 15px 50px rgba(0, 0, 0, 0.35);
        }

        .login-icon {
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
            margin-bottom: 28px;
        }

        .message {
            padding: 12px;
            border-radius: 9px;
            margin-bottom: 18px;
            font-size: 13px;
            text-align: center;
        }

        .success {
            color: #8df0b1;
            background: rgba(35, 180, 100, 0.12);
            border: 1px solid rgba(35, 180, 100, 0.3);
        }

        .error {
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

        .login-button {
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

        .login-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 20px rgba(147, 51, 234, 0.3);
        }

        .register-link {
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
            .login-card {
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

<div class="login-container">

    <div class="login-card">

        <div class="login-icon">🎬</div>

        <h1>Welcome Back!</h1>

        <p class="subtitle">
            Login to MovieBook and book your favourite movie tickets.
        </p>

        <% if (justRegistered) { %>
            <div class="message success">
                Registration successful! Please login.
            </div>
        <% } %>

        <% if (error != null) { %>
            <div class="message error">
                <%= HtmlUtil.esc(error) %>
            </div>
        <% } %>

        <form action="<%= context %>/LoginServlet" method="post">

            <input type="hidden"
                   name="redirect"
                   value="<%= HtmlUtil.esc(redirect) %>">

            <label for="email">Email Address</label>

            <input class="input-field"
                   type="email"
                   id="email"
                   name="email"
                   placeholder="you@example.com"
                   value="<%= HtmlUtil.esc(email) %>"
                   required>

            <label for="password">Password</label>

            <input class="input-field"
                   type="password"
                   id="password"
                   name="password"
                   placeholder="Enter your password"
                   required>

            <button type="submit" class="login-button">
                Login to MovieBook
            </button>

        </form>

        <p class="register-link">
            New to MovieBook?
            <a href="<%= context %>/register.jsp">Create an account</a>
        </p>

        <a class="back-link"
           href="<%= context %>/index.jsp">
            ← Back to Homepage
        </a>

    </div>

</div>

</body>
</html>
