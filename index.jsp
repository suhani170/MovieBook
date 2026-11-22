
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, dao.MovieDAO, model.Movie, util.HtmlUtil"%>

<%
    List<Movie> movies = new MovieDAO().getAllMovies();

    String navUserName = (String) session.getAttribute("userName");
    String adminName = (String) session.getAttribute("adminUsername");

    Movie featured = (movies != null && !movies.isEmpty())
            ? movies.get(0) : null;
%>

<!DOCTYPE html>
<html>
<head>
    <title>MovieBook | Discover Cinema</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">

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

        .admin-btn:hover {
            background: #8254ee;
            color: white !important;
            box-shadow: 0 5px 20px #8254ee55;
        }

        /* HERO */

        .hero {
            min-height: 510px;
            padding: 80px 8%;
            display: flex;
            align-items: center;
            position: relative;
            overflow: hidden;
            background:
                radial-gradient(ellipse at 80% 50%, #402078 0%, transparent 45%),
                linear-gradient(110deg, #100d1c, #171026);
        }

        .hero-content {
            max-width: 650px;
            position: relative;
            z-index: 2;
        }

        .hero-tag {
            display: inline-block;
            padding: 9px 15px;
            border: 1px solid #6243a0;
            background: #24183c;
            color: #c4aaff;
            border-radius: 30px;
            font-size: 12px;
            margin-bottom: 24px;
            letter-spacing: 1px;
        }

        .hero h1 {
            font-size: clamp(38px, 5vw, 65px);
            line-height: 1.12;
            font-weight: 800;
            margin-bottom: 20px;
        }

        .hero h1 span {
            color: #a78bfa;
        }

        .hero p {
            color: #b5afc3;
            font-size: 16px;
            line-height: 1.8;
            margin-bottom: 30px;
            max-width: 510px;
        }

        .browse-btn {
            display: inline-block;
            padding: 15px 25px;
            background: #8755ef;
            border-radius: 10px;
            font-weight: 700;
            transition: .3s;
        }

        .browse-btn:hover {
            background: #a78bfa;
            transform: translateY(-3px);
        }

        .hero-poster {
            position: absolute;
            right: 12%;
            top: 45px;
            width: 260px;
            height: 390px;
            object-fit: cover;
            border-radius: 18px;
            opacity: .82;
            transform: rotate(5deg);
            box-shadow: 0 25px 80px #000;
            mask-image: linear-gradient(to bottom, black 70%, transparent 100%);
        }

        /* MOVIES */

        .movies-section {
            padding: 75px 7%;
            background: #0c0a14;
        }

        .section-heading {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            margin-bottom: 30px;
        }

        .section-heading h2 {
            font-size: 30px;
            margin-bottom: 8px;
        }

        .section-heading p {
            color: #898397;
            font-size: 14px;
        }

        .search-box {
            background: #171422;
            border: 1px solid #332747;
            border-radius: 10px;
            padding: 13px 17px;
            color: white;
            width: 250px;
            outline: none;
        }

        .search-box:focus {
            border-color: #9568f5;
        }

        .movie-grid {
            display: grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 25px;
        }

        .movie-card {
            background: #171421;
            border: 1px solid #292337;
            border-radius: 15px;
            overflow: hidden;
            transition: .3s;
        }

        .movie-card:hover {
            transform: translateY(-8px);
            border-color: #9364ed;
            box-shadow: 0 15px 40px #0008;
        }

        .poster {
            width: 100%;
            height: 340px;
            object-fit: cover;
            display: block;
            background: linear-gradient(135deg, #342254, #15101e);
        }

        .poster-placeholder {
            height: 340px;
            background: linear-gradient(135deg, #342254, #15101e);
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 50px;
        }

        .movie-info {
            padding: 19px;
        }

        .movie-info h3 {
            font-size: 17px;
            margin-bottom: 10px;
        }

        .movie-meta {
            color: #a78bfa;
            font-size: 12px;
            margin-bottom: 12px;
        }

        .movie-info p {
            color: #9993a8;
            font-size: 13px;
            line-height: 1.6;
            min-height: 42px;
            margin-bottom: 17px;
        }

        .book-btn {
            display: block;
            padding: 12px;
            text-align: center;
            background: #30204b;
            color: #c6adff;
            border-radius: 9px;
            font-weight: 700;
            font-size: 13px;
        }

        .book-btn:hover {
            background: #8254ee;
            color: white;
        }

        .empty {
            grid-column: 1 / -1;
            padding: 65px 20px;
            text-align: center;
            background: #171421;
            border-radius: 15px;
            color: #aaa;
        }

        /* FEATURES */

        .features {
            padding: 70px 7%;
            background: #100d19;
        }

        .features h2 {
            font-size: 29px;
            margin-bottom: 30px;
        }

        .feature-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 22px;
        }

        .feature {
            background: #191525;
            padding: 28px;
            border: 1px solid #30263e;
            border-radius: 15px;
        }

        .feature h3 {
            margin-bottom: 12px;
        }

        .feature p {
            color: #aaa2b9;
            line-height: 1.7;
            font-size: 14px;
        }

        /* FOOTER */

        footer {
            padding: 28px 7%;
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

        .footer-admin {
            color: #c4aaff !important;
            font-weight: 800;
        }

        /* RESPONSIVE */

        @media(max-width: 1000px) {
            .movie-grid {
                grid-template-columns: repeat(3, 1fr);
            }

            .hero-poster {
                right: 5%;
                width: 210px;
            }

            .nav-links {
                gap: 15px;
            }
        }

        @media(max-width: 700px) {
            .navbar {
                height: auto;
                padding: 20px 5%;
                flex-direction: column;
                gap: 18px;
            }

            .nav-links {
                flex-wrap: wrap;
                justify-content: center;
                gap: 15px;
            }

            .hero {
                padding: 65px 7%;
            }

            .hero-poster {
                display: none;
            }

            .movie-grid {
                grid-template-columns: repeat(2, 1fr);
                gap: 13px;
            }

            .poster, .poster-placeholder {
                height: 240px;
            }

            .movie-info {
                padding: 13px;
            }

            .section-heading {
                align-items: flex-start;
                flex-direction: column;
            }

            .search-box {
                width: 100%;
            }

            .feature-grid {
                grid-template-columns: 1fr;
            }

            footer {
                flex-direction: column;
                text-align: center;
            }

            .footer-links {
                justify-content: center;
            }
        }

        @media(max-width: 420px) {
            .movie-grid {
                grid-template-columns: 1fr;
            }

            .poster, .poster-placeholder {
                height: 350px;
            }
        }
    </style>
</head>

<body>

<!-- NAVBAR -->

<nav class="navbar">

    <a href="index.jsp" class="logo">MovieBook.</a>

    <div class="nav-links">

        <a href="#movies">Movies</a>

        <a href="#features">About</a>

        <% if (navUserName != null) { %>

            <a href="MyBookings">My Bookings</a>

            <span style="color:#b99aff;">
                <%= HtmlUtil.esc(navUserName) %>
            </span>

            <a href="LogoutServlet" class="nav-btn">Logout</a>

        <% } else { %>

            <a href="customer-login.jsp">Login</a>
            <a href="register.jsp" class="nav-btn">Register</a>

        <% } %>

        <% if (adminName != null) { %>

            <a href="admin-dashboard.jsp" class="admin-btn">
                Admin Dashboard
            </a>

        <% } else { %>

            <a href="admin-login.jsp" class="admin-btn">
                🔐 Admin Login
            </a>

        <% } %>

    </div>

</nav>

<!-- HERO -->

<section class="hero">

    <div class="hero-content">

        <span class="hero-tag">YOUR CINEMA EXPERIENCE STARTS HERE</span>

        <h1>
            Movies are better
            <span>on the big screen.</span>
        </h1>

        <p>
            Discover your next favourite movie, explore available
            shows and reserve your perfect seats with MovieBook.
        </p>

        <a href="#movies" class="browse-btn">
            Explore Movies →
        </a>

    </div>

    <% if (featured != null &&
          featured.getPosterUrl() != null &&
          !featured.getPosterUrl().trim().isEmpty()) { %>

        <img class="hero-poster"
             src="<%= HtmlUtil.esc(featured.getPosterUrl()) %>"
             alt="Featured movie poster">

    <% } %>

</section>

<!-- MOVIES -->

<section class="movies-section" id="movies">

    <div class="section-heading">

        <div>
            <h2>Explore Movies</h2>
            <p>Find your next cinema experience.</p>
        </div>

        <input type="text"
               class="search-box"
               id="movieSearch"
               placeholder="Search movies...">

    </div>

    <div class="movie-grid" id="movieGrid">

        <% if (movies == null || movies.isEmpty()) { %>

            <div class="empty">
                <h3>No movies available</h3>
                <p>Check back later for upcoming movies.</p>
            </div>

        <% } else {

            for (Movie m : movies) {

                String description = m.getDescription() == null
                        ? "" : m.getDescription();

                if (description.length() > 100) {
                    description = description.substring(0, 100) + "...";
                }
        %>

            <article class="movie-card"
                     data-title="<%= HtmlUtil.esc(m.getTitle()) %>"
                     data-genre="<%= HtmlUtil.esc(m.getGenre()) %>">

                <% if (m.getPosterUrl() != null &&
                       !m.getPosterUrl().trim().isEmpty()) { %>

                    <img class="poster"
                         src="<%= HtmlUtil.esc(m.getPosterUrl()) %>"
                         alt="<%= HtmlUtil.esc(m.getTitle()) %> poster"
                         loading="lazy"
                         onerror="this.style.display='none';this.nextElementSibling.style.display='flex';">

                    <div class="poster-placeholder" style="display:none;">
                        🎬
                    </div>

                <% } else { %>

                    <div class="poster-placeholder">
                        🎬
                    </div>

                <% } %>

                <div class="movie-info">

                    <h3><%= HtmlUtil.esc(m.getTitle()) %></h3>

                    <div class="movie-meta">
                        <%= HtmlUtil.esc(m.getGenre()) %>
                        &nbsp; • &nbsp;
                        <%= HtmlUtil.esc(m.getLanguage()) %>
                        &nbsp; • &nbsp;
                        <%= m.getDuration() %> min
                    </div>

                    <p><%= HtmlUtil.esc(description) %></p>

                    <a class="book-btn"
                       href="MovieShows?movieId=<%= m.getMovieId() %>">
                        Book Tickets →
                    </a>

                </div>

            </article>

        <% } } %>

    </div>

</section>

<!-- FEATURES -->

<section class="features" id="features">

    <h2>Why MovieBook?</h2>

    <div class="feature-grid">

        <div class="feature">
            <h3>🎬 Easy Booking</h3>
            <p>Browse available movies and book your tickets conveniently.</p>
        </div>

        <div class="feature">
            <h3>💺 Choose Your Seats</h3>
            <p>Select your preferred seats before confirming your booking.</p>
        </div>

        <div class="feature">
            <h3>🎟️ Booking History</h3>
            <p>Access your bookings and review your cinema reservations.</p>
        </div>

    </div>

</section>

<!-- FOOTER -->

<footer>

    <div>
        MovieBook | Your movie experience, reimagined.
    </div>

    <div class="footer-links">

        <a href="index.jsp">Home</a>

        <a href="#movies">Movies</a>

        <a href="customer-login.jsp">Customer Login</a>

        <% if (adminName != null) { %>

            <a href="admin-dashboard.jsp" class="footer-admin">
                Admin Dashboard
            </a>

        <% } else { %>

            <a href="admin-login.jsp" class="footer-admin">
                🔐 Admin Login
            </a>

        <% } %>

    </div>

</footer>

<!-- MOVIE SEARCH -->

<script>
    var search = document.getElementById("movieSearch");

    search.addEventListener("input", function () {

        var value = this.value.toLowerCase();

        document.querySelectorAll(".movie-card").forEach(function(card) {

            var title = card.dataset.title.toLowerCase();
            var genre = card.dataset.genre.toLowerCase();

            card.style.display =
                title.includes(value) || genre.includes(value)
                ? "" : "none";
        });
    });
</script>

</body>
</html>
