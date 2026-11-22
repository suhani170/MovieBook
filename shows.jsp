
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, model.Movie, model.ShowInfo, util.HtmlUtil"%>
<%
    Movie movie = (Movie) request.getAttribute("movie");
    List<ShowInfo> shows = (List<ShowInfo>) request.getAttribute("shows");

    String context = request.getContextPath();

    if (movie == null) {
        response.sendRedirect(context + "/index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title><%= HtmlUtil.esc(movie.getTitle()) %> - MovieBook</title>
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

        .shows-container {
            max-width: 1050px;
            margin: auto;
            padding: 45px 22px 65px;
        }

        .movie-heading {
            padding: 30px;
            background: linear-gradient(135deg, #211633, #171421);
            border: 1px solid #30283f;
            border-radius: 18px;
            margin-bottom: 35px;
        }

        .movie-heading h1 {
            font-size: 32px;
            margin: 0 0 17px;
        }

        .tags {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 18px;
        }

        .tags span {
            padding: 7px 13px;
            border-radius: 20px;
            background: #302344;
            color: #d8b4fe;
            font-size: 12px;
            border: 1px solid #493361;
        }

        .description {
            color: #b9b3c6;
            line-height: 1.7;
            font-size: 14px;
            margin: 0;
        }

        .section-heading {
            font-size: 23px;
            margin: 0 0 20px;
        }

        .show-list {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .show-card {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 20px;
            padding: 23px;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 15px;
            transition: 0.2s;
        }

        .show-card:hover {
            border-color: #9254d4;
            transform: translateY(-2px);
        }

        .show-card h3 {
            margin: 0 0 12px;
            font-size: 18px;
        }

        .meta {
            color: #aaa5b7;
            font-size: 13px;
            line-height: 2;
        }

        .price {
            color: #d8b4fe;
            font-size: 19px;
            font-weight: 700;
            margin-bottom: 13px;
            text-align: right;
        }

        .select-button {
            display: inline-block;
            padding: 11px 20px;
            border-radius: 9px;
            background: linear-gradient(135deg, #9333ea, #6d28d9);
            color: white;
            text-decoration: none;
            font-size: 13px;
            font-weight: 700;
            transition: 0.2s;
        }

        .select-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 18px rgba(147, 51, 234, 0.3);
        }

        .empty-card {
            padding: 40px 20px;
            text-align: center;
            background: #171421;
            border: 1px solid #30283f;
            border-radius: 15px;
            color: #aaa5b7;
        }

        .empty-icon {
            font-size: 42px;
            margin-bottom: 12px;
        }

        .back-link {
            display: inline-block;
            margin-top: 30px;
            color: #c084fc;
            text-decoration: none;
            font-size: 14px;
        }

        .back-link:hover {
            color: #e9d5ff;
        }

        @media (max-width: 600px) {
            .movie-heading {
                padding: 22px;
            }

            .movie-heading h1 {
                font-size: 25px;
            }

            .show-card {
                align-items: flex-start;
                flex-direction: column;
            }

            .price {
                text-align: left;
            }
        }
    </style>
</head>

<body>

<%@ include file="includes/navbar.jspf" %>

<div class="shows-container">

    <div class="movie-heading">

        <h1>🎬 <%= HtmlUtil.esc(movie.getTitle()) %></h1>

        <div class="tags">
            <span><%= HtmlUtil.esc(movie.getGenre()) %></span>
            <span><%= HtmlUtil.esc(movie.getLanguage()) %></span>
            <span><%= movie.getDuration() %> min</span>
        </div>

        <p class="description">
            <%= HtmlUtil.esc(movie.getDescription()) %>
        </p>

    </div>

    <h2 class="section-heading">Choose Your Show</h2>

    <% if (shows == null || shows.isEmpty()) { %>

        <div class="empty-card">
            <div class="empty-icon">📅</div>
            <h3>No Upcoming Shows</h3>
            <p>
                There are currently no upcoming shows for this movie.
                Please check back later.
            </p>
        </div>

    <% } else { %>

        <div class="show-list">

            <% for (ShowInfo s : shows) { %>

                <div class="show-card">

                    <div>
                        <h3>🎭 <%= HtmlUtil.esc(s.getTheatreName()) %></h3>

                        <div class="meta">
                            📍 <%= HtmlUtil.esc(s.getLocation()) %><br>
                            📅 <%= HtmlUtil.esc(s.getShowDate()) %><br>
                            🕒 <%= HtmlUtil.esc(s.getShowTime()) %>
                        </div>
                    </div>

                    <div>
                        <div class="price">
                            ₹<%= String.format("%.2f", s.getTicketPrice()) %>
                            / seat
                        </div>

                        <a class="select-button"
                           href="<%= context %>/SeatSelection?showId=<%= s.getShowId() %>">
                            Select Seats →
                        </a>
                    </div>

                </div>

            <% } %>

        </div>

    <% } %>

    <a class="back-link"
       href="<%= context %>/index.jsp#movies">
        ← Back to All Movies
    </a>

</div>

</body>
</html>
