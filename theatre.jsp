
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String context = request.getContextPath();

    String adminName = (String) session.getAttribute("adminUsername");

    if (adminName == null || adminName.trim().isEmpty()) {
        response.sendRedirect(context + "/admin-login.jsp");
        return;
    }

    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Add Theatre - MovieBook Admin</title>
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

        .admin-container {
            min-height: 80vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 20px;
        }

        .theatre-card {
            width: 100%;
            max-width: 500px;
            padding: 36px;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 20px;
            box-shadow: 0 15px 50px rgba(0, 0, 0, 0.35);
        }

        .theatre-icon {
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
            margin-bottom: 28px;
        }

        .message {
            padding: 12px 14px;
            margin-bottom: 18px;
            border-radius: 9px;
            font-size: 13px;
            line-height: 1.5;
        }

        .error {
            background: #421c2a;
            border: 1px solid #813449;
            color: #ffb3c0;
        }

        label {
            display: block;
            margin: 18px 0 8px;
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

        .submit-button {
            width: 100%;
            margin-top: 26px;
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

        .submit-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 20px rgba(147, 51, 234, 0.3);
        }

        .navigation-links {
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 15px;
            margin-top: 25px;
            font-size: 13px;
        }

        .navigation-links a {
            color: #c084fc;
            text-decoration: none;
        }

        .navigation-links a:hover {
            color: #e9d5ff;
        }

        @media (max-width: 480px) {
            .theatre-card {
                padding: 27px 22px;
            }
        }
    </style>
</head>

<body>

<%@ include file="includes/navbar.jspf" %>

<div class="admin-container">

    <div class="theatre-card">

        <div class="theatre-icon">&#127916;</div>

        <h1>Add New Theatre</h1>

        <p class="subtitle">
            Enter the theatre details to add it to MovieBook.
        </p>

        <% if ("empty".equals(error)) { %>
            <div class="message error">
                Please enter both theatre name and location.
            </div>
        <% } else if ("failed".equals(error)) { %>
            <div class="message error">
                Theatre could not be added. Please check the details and try again.
            </div>
        <% } %>

        <form action="<%= context %>/TheatreServlet" method="post">

            <label for="theatreName">Theatre Name</label>

            <input class="input-field"
                   type="text"
                   id="theatreName"
                   name="theatreName"
                   placeholder="Enter theatre name"
                   maxlength="100"
                   required>

            <label for="location">Location</label>

            <input class="input-field"
                   type="text"
                   id="location"
                   name="location"
                   placeholder="Enter theatre location"
                   maxlength="150"
                   required>

            <button type="submit" class="submit-button">
                Add Theatre &#8594;
            </button>

        </form>

        <div class="navigation-links">

            <a href="<%= context %>/admin-dashboard.jsp">
                &#8592; Admin Dashboard
            </a>

            <a href="<%= context %>/TheatreListServlet">
                View Theatres &#8594;
            </a>

        </div>

    </div>

</div>

</body>
</html>
