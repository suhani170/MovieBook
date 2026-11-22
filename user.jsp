
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String context = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>User Registration - MovieBook</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

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
            padding: 36px;
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
            font-size: 26px;
            margin: 0 0 10px;
        }

        .subtitle {
            text-align: center;
            color: #aaa5b7;
            font-size: 13px;
            margin-bottom: 27px;
        }

        label {
            display: block;
            margin: 17px 0 8px;
            color: #ded9e9;
            font-size: 13px;
            font-weight: 600;
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

        .register-button {
            width: 100%;
            margin-top: 25px;
            padding: 14px;
            border: none;
            border-radius: 10px;
            background: linear-gradient(135deg, #9333ea, #6d28d9);
            color: #fff;
            font-size: 15px;
            font-weight: 700;
            cursor: pointer;
            transition: 0.2s;
        }

        .register-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 20px rgba(147, 51, 234, 0.3);
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 23px;
            color: #c084fc;
            text-decoration: none;
            font-size: 13px;
        }

        .back-link:hover {
            color: #e9d5ff;
        }

        @media (max-width: 480px) {
            .register-card {
                padding: 27px 22px;
            }
        }
    </style>
</head>

<body>

<%@ include file="includes/navbar.jspf" %>

<div class="register-container">

    <div class="register-card">

        <div class="register-icon">👤</div>

        <h1>User Registration</h1>

        <p class="subtitle">
            Enter your details to register with MovieBook.
        </p>

        <form action="<%= context %>/UserServlet" method="post">

            <label for="name">Full Name</label>

            <input class="input-field"
                   type="text"
                   id="name"
                   name="name"
                   placeholder="Enter your name"
                   required>

            <label for="email">Email Address</label>

            <input class="input-field"
                   type="email"
                   id="email"
                   name="email"
                   placeholder="Enter your email"
                   required>

            <label for="password">Password</label>

            <input class="input-field"
                   type="password"
                   id="password"
                   name="password"
                   placeholder="Enter your password"
                   required>

            <button type="submit" class="register-button">
                Register User →
            </button>

        </form>

        <a class="back-link"
           href="<%= context %>/index.jsp">
            ← Back to Homepage
        </a>

    </div>

</div>

</body>
</html>
