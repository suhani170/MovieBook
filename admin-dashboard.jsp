
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String context = request.getContextPath();
    String adminName = (String) session.getAttribute("adminUsername");

    if (adminName == null || adminName.trim().isEmpty()) {
        response.sendRedirect(context + "/admin-login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | MovieBook</title>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: #090810;
            color: #fff;
        }

        a {
            text-decoration: none;
            color: inherit;
        }

        /* SIDEBAR */

        .sidebar {
            position: fixed;
            left: 0;
            top: 0;
            width: 250px;
            height: 100vh;
            padding: 25px 18px;
            background: #0f0d1a;
            border-right: 1px solid #292337;
            z-index: 100;
            display: flex;
            flex-direction: column;
        }

        .logo {
            font-size: 25px;
            font-weight: 800;
            color: #a78bfa;
            letter-spacing: -1px;
            padding: 10px;
            margin-bottom: 38px;
        }

        .nav-title {
            color: #81798d;
            font-size: 11px;
            font-weight: 800;
            padding: 0 12px;
            margin-bottom: 13px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .nav {
            display: flex;
            flex-direction: column;
            gap: 7px;
        }

        .nav a {
            display: block;
            color: #c9c5d2;
            padding: 14px;
            border-radius: 9px;
            font-size: 14px;
            transition: .25s;
        }

        .nav a:hover {
            background: #29253a;
            color: white;
        }

        .nav a.active {
            background: #30204b;
            color: #c6adff;
            border: 1px solid #6544a0;
            font-weight: 700;
        }

        .sidebar-bottom {
            margin-top: auto;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .sidebar-bottom a {
            display: block;
            text-align: center;
            padding: 12px;
            border: 1px solid #403b4d;
            border-radius: 9px;
            color: #c9c5d2;
            font-size: 13px;
            transition: .25s;
        }

        .sidebar-bottom a:hover {
            background: #29253a;
            color: white;
        }

        .sidebar-bottom .website-link {
            border-color: #6544a0;
            color: #c6adff;
        }

        /* MAIN */

        .main {
            margin-left: 250px;
            padding: 35px 5%;
            min-height: 100vh;
            background:
                radial-gradient(ellipse at 85% 5%, #291743 0, transparent 30%),
                #090810;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            margin-bottom: 38px;
            padding-bottom: 25px;
            border-bottom: 1px solid #292337;
        }

        .topbar h1 {
            font-size: 30px;
            font-weight: 800;
            margin-bottom: 8px;
        }

        .welcome {
            color: #aaa2b9;
            font-size: 14px;
        }

        .admin {
            background: #171421;
            border: 1px solid #493365;
            padding: 12px 18px;
            border-radius: 25px;
            color: #c6adff;
            font-size: 13px;
            font-weight: 700;
            white-space: nowrap;
        }

        /* OVERVIEW */

        .overview {
            margin-bottom: 38px;
        }

        .section-title {
            font-size: 21px;
            margin-bottom: 20px;
            font-weight: 800;
        }

        .stats {
            display: grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 20px;
        }

        .stat-card {
            background: #171421;
            padding: 23px;
            border: 1px solid #292337;
            border-radius: 14px;
            transition: .25s;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            border-color: #8254ee;
            box-shadow: 0 12px 30px #0006;
        }

        .stat-icon {
            width: 48px;
            height: 48px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 12px;
            background: #30204b;
            font-size: 24px;
            margin-bottom: 17px;
        }

        .stat-card h3 {
            font-size: 17px;
            margin-bottom: 8px;
        }

        .stat-card p {
            color: #9993a8;
            font-size: 13px;
            line-height: 1.6;
        }

        .stat-card a {
            display: inline-block;
            margin-top: 17px;
            color: #b99aff;
            font-size: 13px;
            font-weight: 700;
        }

        .stat-card a:hover {
            color: white;
        }

        /* MANAGEMENT */

        .management {
            margin-bottom: 35px;
        }

        .management-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 20px;
        }

        .manage-card {
            background: #171421;
            padding: 25px;
            border: 1px solid #292337;
            border-radius: 14px;
            transition: .25s;
        }

        .manage-card:hover {
            transform: translateY(-5px);
            border-color: #9364ed;
            box-shadow: 0 12px 30px #0007;
        }

        .big-icon {
            font-size: 34px;
            margin-bottom: 17px;
        }

        .manage-card h3 {
            font-size: 17px;
            margin-bottom: 10px;
        }

        .manage-card p {
            color: #9993a8;
            font-size: 13px;
            line-height: 1.7;
            min-height: 44px;
            margin-bottom: 20px;
        }

        .manage-card a {
            display: inline-block;
            padding: 11px 16px;
            background: #30204b;
            color: #c6adff;
            border: 1px solid #493365;
            border-radius: 9px;
            font-size: 13px;
            font-weight: 700;
            transition: .25s;
        }

        .manage-card a:hover {
            background: #8254ee;
            color: white;
            border-color: #8254ee;
        }

        /* FOOTER */

        .footer {
            padding: 22px 0;
            margin-top: 35px;
            border-top: 1px solid #292337;
            color: #81798d;
            font-size: 12px;
            display: flex;
            justify-content: space-between;
            gap: 15px;
            flex-wrap: wrap;
        }

        .footer a {
            color: #b99aff;
        }

        .footer a:hover {
            color: white;
        }

        /* RESPONSIVE */

        @media(max-width: 1100px) {
            .stats {
                grid-template-columns: repeat(2, 1fr);
            }

            .management-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media(max-width: 700px) {
            .sidebar {
                position: relative;
                width: 100%;
                height: auto;
                padding: 15px;
                border-right: none;
                border-bottom: 1px solid #292337;
            }

            .logo {
                margin-bottom: 20px;
                text-align: center;
            }

            .nav-title {
                text-align: center;
            }

            .nav {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
            }

            .nav a {
                text-align: center;
                padding: 12px 8px;
                font-size: 12px;
            }

            .sidebar-bottom {
                margin-top: 18px;
                display: grid;
                grid-template-columns: repeat(2, 1fr);
            }

            .main {
                margin-left: 0;
                padding: 25px 18px;
            }

            .topbar {
                align-items: flex-start;
                flex-direction: column;
            }

            .topbar h1 {
                font-size: 25px;
            }

            .stats,
            .management-grid {
                grid-template-columns: 1fr;
            }

            .footer {
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

    <!-- SIDEBAR -->

    <aside class="sidebar">

        <a href="<%= context %>/index.jsp" class="logo">
            MovieBook.
        </a>

        <div class="nav-title">Administration</div>

        <nav class="nav">

            <a href="<%= context %>/admin-dashboard.jsp" class="active">
                📊 Dashboard
            </a>

            <a href="<%= context %>/MovieListServlet">
                🎬 Movies
            </a>

            <a href="<%= context %>/TheatreListServlet">
                🎭 Theatres
            </a>

            <a href="<%= context %>/ShowListServlet">
                🎥 Shows
            </a>

            <a href="<%= context %>/UserListServlet">
                👤 Users
            </a>

            <a href="<%= context %>/BookingListServlet">
                🎟️ Bookings
            </a>

        </nav>

        <div class="sidebar-bottom">

            <a href="<%= context %>/index.jsp" class="website-link">
                🏠 View Website
            </a>

            <a href="<%= context %>/LogoutServlet">
                🚪 Logout
            </a>

        </div>

    </aside>

    <!-- MAIN CONTENT -->

    <main class="main">

        <header class="topbar">

            <div>
                <h1>Dashboard</h1>

                <p class="welcome">
                    Welcome back, <%= util.HtmlUtil.esc(adminName) %> 👋
                </p>
            </div>

            <div class="admin">
                🔐 Administrator
            </div>

        </header>

        <!-- OVERVIEW -->

        <section class="overview">

            <h2 class="section-title">Quick Overview</h2>

            <div class="stats">

                <div class="stat-card">
                    <div class="stat-icon">🎬</div>
                    <h3>Movies</h3>
                    <p>Manage your movie collection and movie information.</p>
                    <a href="<%= context %>/MovieListServlet">
                        View Movies →
                    </a>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">🎭</div>
                    <h3>Theatres</h3>
                    <p>Manage cinema theatres and their information.</p>
                    <a href="<%= context %>/TheatreListServlet">
                        View Theatres →
                    </a>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">🎥</div>
                    <h3>Shows</h3>
                    <p>Manage movie show schedules and ticket prices.</p>
                    <a href="<%= context %>/ShowListServlet">
                        View Shows →
                    </a>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">🎟️</div>
                    <h3>Bookings</h3>
                    <p>Review customer ticket booking information.</p>
                    <a href="<%= context %>/BookingListServlet">
                        View Bookings →
                    </a>
                </div>

            </div>

        </section>

        <!-- MANAGEMENT -->

        <section class="management">

            <h2 class="section-title">Management</h2>

            <div class="management-grid">

                <div class="manage-card">
                    <div class="big-icon">🎬</div>
                    <h3>Movie Management</h3>
                    <p>
                        Add, edit, view and delete movies from the system.
                    </p>
                    <a href="<%= context %>/MovieListServlet">
                        Manage Movies →
                    </a>
                </div>

                <div class="manage-card">
                    <div class="big-icon">🎭</div>
                    <h3>Theatre Management</h3>
                    <p>
                        Manage theatres and update cinema information.
                    </p>
                    <a href="<%= context %>/TheatreListServlet">
                        Manage Theatres →
                    </a>
                </div>

                <div class="manage-card">
                    <div class="big-icon">🎥</div>
                    <h3>Show Management</h3>
                    <p>
                        Create and manage movie show schedules.
                    </p>
                    <a href="<%= context %>/ShowListServlet">
                        Manage Shows →
                    </a>
                </div>

                <div class="manage-card">
                    <div class="big-icon">👤</div>
                    <h3>User Management</h3>
                    <p>
                        View registered customers and their information.
                    </p>
                    <a href="<%= context %>/UserListServlet">
                        Manage Users →
                    </a>
                </div>

                <div class="manage-card">
                    <div class="big-icon">🎟️</div>
                    <h3>Booking Management</h3>
                    <p>
                        View and manage customer ticket bookings.
                    </p>
                    <a href="<%= context %>/BookingListServlet">
                        Manage Bookings →
                    </a>
                </div>

            </div>

        </section>

        <!-- FOOTER -->

        <footer class="footer">

            <span>MovieBook | Your movie experience, reimagined.</span>

            <a href="<%= context %>/index.jsp">
                Back to Homepage →
            </a>

        </footer>

    </main>

</body>
</html>
