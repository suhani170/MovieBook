
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String context = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Book Movie Ticket - MovieBook</title>
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

        .booking-container {
            min-height: 78vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 20px;
        }

        .booking-card {
            width: 100%;
            max-width: 480px;
            padding: 35px;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 20px;
            box-shadow: 0 15px 50px rgba(0, 0, 0, 0.35);
        }

        .booking-icon {
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
            margin-bottom: 25px;
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
            padding: 13px;
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

        .booking-button {
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

        .booking-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 20px rgba(147, 51, 234, 0.3);
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 22px;
            color: #c084fc;
            text-decoration: none;
            font-size: 13px;
        }

        .back-link:hover {
            color: #e9d5ff;
        }

        @media (max-width: 480px) {
            .booking-card {
                padding: 27px 22px;
            }
        }
    </style>
</head>

<body>

<%@ include file="includes/navbar.jspf" %>

<div class="booking-container">

    <div class="booking-card">

        <div class="booking-icon">🎟️</div>

        <h1>Book Movie Ticket</h1>

        <p class="subtitle">
            Enter your booking details below.
        </p>

        <form action="<%= context %>/BookingServlet" method="post">

            <label for="userId">User ID</label>
            <input class="input-field"
                   type="number"
                   id="userId"
                   name="userId"
                   required>

            <label for="showId">Show ID</label>
            <input class="input-field"
                   type="number"
                   id="showId"
                   name="showId"
                   required>

            <label for="seatNumber">Seat Number</label>
            <input class="input-field"
                   type="text"
                   id="seatNumber"
                   name="seatNumber"
                   placeholder="Example: A1"
                   required>

            <label for="totalAmount">Total Amount (₹)</label>
            <input class="input-field"
                   type="number"
                   id="totalAmount"
                   name="totalAmount"
                   step="0.01"
                   min="0"
                   placeholder="Example: 250.00"
                   required>

            <button type="submit" class="booking-button">
                Book Ticket →
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
