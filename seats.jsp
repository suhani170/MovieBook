<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Set, model.ShowInfo, util.HtmlUtil"%>
<%
    ShowInfo show = (ShowInfo) request.getAttribute("show");
    Set<String> bookedSeats = (Set<String>) request.getAttribute("bookedSeats");
    String rows = (String) request.getAttribute("rows");
    Integer seatsPerRowValue = (Integer) request.getAttribute("seatsPerRow");
    String error = (String) request.getAttribute("error");

    String context = request.getContextPath();

    if (show == null || bookedSeats == null || rows == null
            || seatsPerRowValue == null) {
        response.sendRedirect(context + "/index.jsp");
        return;
    }

    int seatsPerRow = seatsPerRowValue;
    int totalSeats = rows.length() * seatsPerRow;
    int availableSeats = totalSeats - bookedSeats.size();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Select Seats - MovieBook</title>
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
            background: #090810;
            color: #fff;
            font-family: 'Inter', sans-serif;
        }

        .seats-container {
            max-width: 1050px;
            margin: auto;
            padding: 40px 20px 60px;
        }

        .page-heading {
            margin-bottom: 25px;
        }

        .page-heading h1 {
            font-size: 30px;
            margin: 0 0 10px;
        }

        .page-heading p {
            color: #aaa5b7;
            font-size: 14px;
        }

        .show-info {
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 15px;
            padding: 22px;
            margin-bottom: 25px;
        }

        .show-info h2 {
            margin: 0 0 13px;
            font-size: 20px;
        }

        .show-details {
            display: flex;
            flex-wrap: wrap;
            gap: 12px 22px;
            color: #b9b3c6;
            font-size: 13px;
            line-height: 1.7;
        }

        .price-info {
            color: #d8b4fe;
            font-weight: 700;
            margin-top: 15px;
            font-size: 15px;
        }

        .error-message {
            padding: 13px;
            margin-bottom: 20px;
            border-radius: 9px;
            color: #ff9999;
            background: rgba(255, 60, 60, 0.1);
            border: 1px solid rgba(255, 60, 60, 0.3);
            font-size: 13px;
        }

        .seat-panel {
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 18px;
            padding: 30px 20px;
        }

        .seat-panel h2 {
            text-align: center;
            font-size: 21px;
            margin: 0 0 8px;
        }

        .seat-instruction {
            text-align: center;
            color: #aaa5b7;
            font-size: 13px;
            margin-bottom: 35px;
        }

        .screen {
            max-width: 550px;
            height: 9px;
            margin: 0 auto 55px;
            background: linear-gradient(90deg, #6d28d9, #c084fc, #6d28d9);
            border-radius: 50%;
            box-shadow: 0 8px 25px rgba(168, 85, 247, 0.3);
            position: relative;
        }

        .screen::after {
            content: "SCREEN THIS WAY";
            position: absolute;
            top: 18px;
            left: 0;
            right: 0;
            text-align: center;
            color: #aaa5b7;
            font-size: 10px;
            letter-spacing: 3px;
        }

        .seat-grid {
            width: 100%;
            overflow-x: auto;
            padding-bottom: 10px;
        }

        .seat-row {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 9px;
            margin-bottom: 13px;
            min-width: max-content;
        }

        .row-label {
            width: 22px;
            color: #c084fc;
            font-size: 13px;
            font-weight: 700;
            text-align: center;
            margin-right: 8px;
        }

        .seat {
            width: 37px;
            height: 34px;
            position: relative;
            cursor: pointer;
        }

        .seat input {
            position: absolute;
            opacity: 0;
            cursor: pointer;
        }

        .seat span {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #29243a;
            border: 1px solid #514766;
            border-radius: 8px 8px 5px 5px;
            color: #ddd6e9;
            font-size: 11px;
            transition: 0.2s;
        }

        .seat:hover span {
            border-color: #c084fc;
            background: #3b2b53;
        }

        .seat input:checked + span {
            background: #9333ea;
            border-color: #c084fc;
            color: #fff;
            box-shadow: 0 0 12px rgba(147, 51, 234, 0.35);
        }

        .seat input:disabled + span {
            background: #3a1a22;
            border-color: #5a2630;
            color: #b58c91;
            cursor: not-allowed;
        }

        .seat input:focus-visible + span {
            outline: 2px solid #fff;
            outline-offset: 3px;
        }

        .legend {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 22px;
            margin: 35px 0;
            color: #b9b3c6;
            font-size: 12px;
        }

        .legend-item {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .legend-box {
            width: 17px;
            height: 17px;
            border-radius: 5px;
        }

        .available-box {
            background: #29243a;
            border: 1px solid #514766;
        }

        .selected-box {
            background: #9333ea;
        }

        .booked-box {
            background: #3a1a22;
            border: 1px solid #5a2630;
        }

        .summary {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            padding: 20px;
            background: #0e0c16;
            border: 1px solid #30283f;
            border-radius: 12px;
        }

        .total {
            color: #aaa5b7;
            font-size: 13px;
            line-height: 2;
        }

        .total b {
            color: #fff;
        }

        #totalAmount {
            color: #d8b4fe;
            font-size: 19px;
        }

        .book-button {
            padding: 13px 22px;
            border: none;
            border-radius: 9px;
            background: linear-gradient(135deg, #9333ea, #6d28d9);
            color: #fff;
            font-weight: 700;
            cursor: pointer;
            transition: 0.2s;
        }

        .book-button:disabled {
            opacity: 0.4;
            cursor: not-allowed;
        }

        .book-button:not(:disabled):hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 18px rgba(147, 51, 234, 0.3);
        }

        .back-link {
            display: inline-block;
            margin-top: 25px;
            color: #c084fc;
            text-decoration: none;
            font-size: 14px;
        }

        @media (max-width: 600px) {
            .page-heading h1 {
                font-size: 25px;
            }

            .seat-panel {
                padding: 25px 10px;
            }

            .seat-row {
                gap: 6px;
            }

            .seat {
                width: 30px;
                height: 30px;
            }

            .summary {
                flex-direction: column;
                align-items: stretch;
            }

            .book-button {
                width: 100%;
            }
        }
    </style>
</head>

<body>

<%@ include file="includes/navbar.jspf" %>

<div class="seats-container">

    <div class="page-heading">
        <h1>🎟️ Select Your Seats</h1>
        <p>Choose your preferred seats and confirm your booking.</p>
    </div>

    <div class="show-info">

        <h2><%= HtmlUtil.esc(show.getMovieTitle()) %></h2>

        <div class="show-details">
            <span>🎭 <%= HtmlUtil.esc(show.getTheatreName()) %></span>
            <span>📍 <%= HtmlUtil.esc(show.getLocation()) %></span>
            <span>📅 <%= HtmlUtil.esc(show.getShowDate()) %></span>
            <span>🕒 <%= HtmlUtil.esc(show.getShowTime()) %></span>
        </div>

        <div class="price-info">
            ₹<%= String.format("%.2f", show.getTicketPrice()) %> per seat
            &nbsp; | &nbsp;
            <%= availableSeats %> of <%= totalSeats %> seats available
        </div>

    </div>

    <% if (error != null) { %>
        <div class="error-message">
            <%= HtmlUtil.esc(error) %>
        </div>
    <% } %>

    <form action="<%= context %>/ConfirmBooking"
          method="post"
          id="bookingForm">

        <input type="hidden"
               name="showId"
               value="<%= show.getShowId() %>">

        <div class="seat-panel">

            <h2>Choose Your Seats</h2>

            <p class="seat-instruction">
                Select up to 6 seats for your booking.
            </p>

            <div class="screen"></div>

            <div class="seat-grid">

                <% for (int r = 0; r < rows.length(); r++) {
                       char rowChar = rows.charAt(r); %>

                    <div class="seat-row">

                        <div class="row-label">
                            <%= rowChar %>
                        </div>

                        <% for (int n = 1; n <= seatsPerRow; n++) {
                               String seatId = "" + rowChar + n;
                               boolean taken = bookedSeats.contains(seatId); %>

                            <label class="seat" title="<%= seatId %>">

                                <input type="checkbox"
                                       name="seat"
                                       value="<%= seatId %>"
                                       <%= taken ? "disabled" : "" %>>

                                <span><%= n %></span>

                            </label>

                        <% } %>

                    </div>

                <% } %>

            </div>

            <div class="legend">

                <div class="legend-item">
                    <span class="legend-box available-box"></span>
                    Available
                </div>

                <div class="legend-item">
                    <span class="legend-box selected-box"></span>
                    Selected
                </div>

                <div class="legend-item">
                    <span class="legend-box booked-box"></span>
                    Booked
                </div>

            </div>

            <div class="summary">

                <div class="total">
                    Selected Seats:
                    <b id="seatList">None</b>
                    <br>

                    Total Amount:
                    <b id="totalAmount">₹0.00</b>
                </div>

                <button type="submit"
                        class="book-button"
                        id="bookBtn"
                        disabled>
                    Confirm Booking →
                </button>

            </div>

        </div>

    </form>

    <a class="back-link"
       href="<%= context %>/MovieShows?movieId=<%= show.getMovieId() %>">
        ← Choose Another Show
    </a>

</div>

<script>
    var PRICE = <%= show.getTicketPrice() %>;
    var MAX_SEATS = 6;

    var boxes = document.querySelectorAll('input[name="seat"]');
    var seatList = document.getElementById('seatList');
    var totalAmount = document.getElementById('totalAmount');
    var bookBtn = document.getElementById('bookBtn');
    var bookingForm = document.getElementById('bookingForm');

    function update() {
        var chosen = [];

        boxes.forEach(function (box) {
            if (box.checked) {
                chosen.push(box.value);
            }
        });

        seatList.textContent =
            chosen.length ? chosen.join(', ') : 'None';

        totalAmount.textContent =
            '\u20B9' + (chosen.length * PRICE).toFixed(2);

        bookBtn.disabled = chosen.length === 0;
    }

    boxes.forEach(function (box) {

        box.addEventListener('change', function () {

            var count = document.querySelectorAll(
                'input[name="seat"]:checked'
            ).length;

            if (count > MAX_SEATS) {
                box.checked = false;

                alert(
                    'You can select a maximum of ' +
                    MAX_SEATS + ' seats.'
                );
            }

            update();
        });

    });

    bookingForm.addEventListener('submit', function (event) {

        var count = document.querySelectorAll(
            'input[name="seat"]:checked'
        ).length;

        if (count === 0 || count > MAX_SEATS) {
            event.preventDefault();
            alert('Please select between 1 and 6 seats.');
        }

    });

    update();
</script>

</body>
</html>
