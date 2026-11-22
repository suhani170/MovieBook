
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>My Bookings - MovieBook</title>
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
            background: #090810;
            color: #fff;
            font-family: 'Inter', sans-serif;
        }

        .bookings-container {
            max-width: 1200px;
            margin: auto;
            padding: 40px 22px 65px;
        }

        .page-heading {
            margin-bottom: 28px;
        }

        .page-heading h1 {
            font-size: 30px;
            margin: 0 0 10px;
        }

        .page-heading p {
            color: #aaa5b7;
            font-size: 14px;
        }

        .success-message {
            padding: 15px 18px;
            margin-bottom: 25px;
            border-radius: 10px;
            color: #8df0b1;
            background: rgba(35, 180, 100, 0.12);
            border: 1px solid rgba(35, 180, 100, 0.3);
            font-size: 13px;
        }

        .empty-card {
            padding: 55px 20px;
            text-align: center;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 17px;
        }

        .empty-icon {
            font-size: 48px;
            margin-bottom: 15px;
        }

        .empty-card h2 {
            font-size: 21px;
        }

        .empty-card p {
            color: #aaa5b7;
            font-size: 14px;
            margin-bottom: 25px;
        }

        .browse-button {
            display: inline-block;
            padding: 12px 22px;
            border-radius: 9px;
            background: linear-gradient(135deg, #9333ea, #6d28d9);
            color: #fff;
            text-decoration: none;
            font-size: 13px;
            font-weight: 700;
        }

        .table-card {
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 15px;
            overflow: hidden;
        }

        .table-wrap {
            width: 100%;
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 850px;
        }

        th {
            padding: 17px 15px;
            text-align: left;
            background: #211a30;
            color: #d8b4fe;
            font-size: 12px;
            font-weight: 700;
            white-space: nowrap;
        }

        td {
            padding: 17px 15px;
            border-top: 1px solid #30283f;
            color: #d4cfdd;
            font-size: 12px;
            line-height: 1.8;
        }

        tr:hover td {
            background: rgba(147, 51, 234, 0.06);
        }

        .booking-id {
            color: #c084fc;
            font-weight: 700;
        }

        .seat-number {
            display: inline-block;
            padding: 5px 10px;
            background: #302344;
            border: 1px solid #493361;
            border-radius: 6px;
            color: #d8b4fe;
            font-weight: 700;
        }

        .amount {
            color: #d8b4fe;
            font-weight: 700;
            white-space: nowrap;
        }

        .total-card {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            margin-top: 22px;
            padding: 20px 23px;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 12px;
        }

        .total-label {
            color: #aaa5b7;
            font-size: 14px;
        }

        .total-value {
            color: #d8b4fe;
            font-size: 22px;
            font-weight: 800;
        }

        .back-link {
            display: inline-block;
            margin-top: 27px;
            color: #c084fc;
            text-decoration: none;
            font-size: 14px;
        }

        .back-link:hover {
            color: #e9d5ff;
        }

        @media (max-width: 600px) {
            .page-heading h1 {
                font-size: 25px;
            }

            .total-card {
                align-items: flex-start;
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<%@ include file="includes/navbar.jspf" %>

<div class="bookings-container">

    <div class="page-heading">
        <h1>🎟️ My Bookings</h1>
        <p>All the movie tickets you have booked with MovieBook.</p>
    </div>

    <c:if test="${not empty param.booked and param.booked.matches('[0-9]{1,2}')}">

        <div class="success-message">
            <strong>Booking Successful!</strong>
            <c:out value="${param.booked}"/> seat(s) booked. Enjoy your movie 🍿
        </div>

    </c:if>

    <c:choose>

        <c:when test="${empty requestScope.bookings}">

            <div class="empty-card">

                <div class="empty-icon">🎬</div>

                <h2>No Bookings Yet</h2>

                <p>
                    You haven't booked any movie tickets yet.
                    Explore movies and plan your next movie experience!
                </p>

                <a class="browse-button"
                   href="${pageContext.request.contextPath}/index.jsp#movies">
                    Browse Movies →
                </a>

            </div>

        </c:when>

        <c:otherwise>

            <c:set var="grandTotal" value="0"/>

            <div class="table-card">

                <div class="table-wrap">

                    <table>

                        <thead>
                            <tr>
                                <th>Booking #</th>
                                <th>Movie</th>
                                <th>Theatre</th>
                                <th>Show Date & Time</th>
                                <th>Seat</th>
                                <th>Amount</th>
                                <th>Booked On</th>
                            </tr>
                        </thead>

                        <tbody>

                            <c:forEach var="b" items="${requestScope.bookings}">

                                <c:set var="grandTotal"
                                       value="${grandTotal + b.totalAmount}"/>

                                <tr>

                                    <td class="booking-id">
                                        #<c:out value="${b.bookingId}"/>
                                    </td>

                                    <td>
                                        <strong>
                                            <c:out value="${b.movieTitle}"/>
                                        </strong>
                                    </td>

                                    <td>
                                        <c:out value="${b.theatreName}"/><br>
                                        <span>
                                            <c:out value="${b.location}"/>
                                        </span>
                                    </td>

                                    <td>
                                        <c:out value="${b.showDate}"/><br>
                                        <c:out value="${b.showTime}"/>
                                    </td>

                                    <td>
                                        <span class="seat-number">
                                            <c:out value="${b.seatNumber}"/>
                                        </span>
                                    </td>

                                    <td class="amount">
                                        ₹<fmt:formatNumber
                                            value="${b.totalAmount}"
                                            minFractionDigits="2"
                                            maxFractionDigits="2"/>
                                    </td>

                                    <td>
                                        <c:out value="${b.bookingDate}"/>
                                    </td>

                                </tr>

                            </c:forEach>

                        </tbody>

                    </table>

                </div>

            </div>

            <div class="total-card">

                <div class="total-label">
                    Total Amount Spent on MovieBook
                </div>

                <div class="total-value">
                    ₹<fmt:formatNumber
                        value="${grandTotal}"
                        minFractionDigits="2"
                        maxFractionDigits="2"/>
                </div>

            </div>

        </c:otherwise>

    </c:choose>

    <a class="back-link"
       href="${pageContext.request.contextPath}/index.jsp#movies">
        ← Back to Movies
    </a>

</div>

</body>
</html>
