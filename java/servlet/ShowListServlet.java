
package servlet;

import dao.ShowDAO;
import model.Show;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ShowListServlet")
public class ShowListServlet extends HttpServlet {

    private ShowDAO showDAO;

    @Override
    public void init() throws ServletException {
        showDAO = new ShowDAO();
    }

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("adminUsername") != null;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        try {

            List<Show> shows = showDAO.getAllShows();

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");

            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Show Management - MovieBook</title>");

            out.println("<style>");

            out.println("*{margin:0;padding:0;box-sizing:border-box;}");

            out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:#fff;min-height:100vh;}");

            out.println("nav{background:#160e27;padding:17px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");

            out.println(".logo{font-size:23px;font-weight:bold;color:#b78aff;text-decoration:none;}");

            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");

            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");

            out.println(".nav-links a:hover,.nav-links a.active{color:#c69cff;}");

            out.println(".container{width:94%;max-width:1250px;margin:40px auto;}");

            out.println(".heading{display:flex;justify-content:space-between;align-items:center;gap:15px;flex-wrap:wrap;margin-bottom:25px;}");

            out.println("h1{font-size:29px;color:#d6baff;}");

            out.println(".add{background:#8b5cf6;color:white;padding:12px 18px;border-radius:8px;text-decoration:none;font-weight:bold;font-size:14px;}");

            out.println(".add:hover{background:#7543d8;}");

            out.println(".table-box{background:#160e27;border:1px solid #38264c;border-radius:12px;overflow-x:auto;}");

            out.println("table{width:100%;border-collapse:collapse;min-width:850px;}");

            out.println("th,td{padding:15px 12px;text-align:center;border-bottom:1px solid #302044;}");

            out.println("th{background:#211433;color:#cba8ff;font-size:12px;text-transform:uppercase;letter-spacing:.4px;}");

            out.println("td{color:#ddd;font-size:14px;}");

            out.println("tr:last-child td{border-bottom:none;}");

            out.println("tr:hover td{background:#20152f;}");

            out.println(".actions{white-space:nowrap;}");

            out.println(".edit,.delete{display:inline-block;padding:8px 11px;border-radius:6px;text-decoration:none;font-size:12px;font-weight:bold;}");

            out.println(".edit{background:#49317a;color:#e4d4ff;}");

            out.println(".delete{background:#54212c;color:#ffaaaa;}");

            out.println(".edit:hover{background:#6545a0;}");

            out.println(".delete:hover{background:#7b2938;}");

            out.println(".empty{text-align:center;padding:40px;color:#aaa;}");

            out.println(".count{color:#aaa;font-size:13px;margin-top:15px;}");

            out.println(".message{padding:13px 16px;margin-bottom:18px;border-radius:8px;background:#183c2a;color:#9ff0bd;}");

            out.println("@media(max-width:600px){.container{margin:25px auto;}h1{font-size:23px;}nav{padding:15px;}}");

            out.println("</style>");
            out.println("</head>");

            out.println("<body>");

            // Admin navigation
            out.println("<nav>");

            out.println("<a class='logo' href='" + context
                    + "/admin-dashboard.jsp'>MovieBook Admin</a>");

            out.println("<div class='nav-links'>");

            out.println("<a href='" + context
                    + "/admin-dashboard.jsp'>Dashboard</a>");

            out.println("<a href='" + context
                    + "/MovieListServlet'>Movies</a>");

            out.println("<a href='" + context
                    + "/TheatreListServlet'>Theatres</a>");

            out.println("<a class='active' href='" + context
                    + "/ShowListServlet'>Shows</a>");

            out.println("<a href='" + context
                    + "/UserListServlet'>Users</a>");

            out.println("<a href='" + context
                    + "/BookingListServlet'>Bookings</a>");

            out.println("<a href='" + context
                    + "/LogoutServlet'>Logout</a>");

            out.println("</div>");
            out.println("</nav>");

            out.println("<main class='container'>");

            out.println("<div class='heading'>");

            out.println("<h1>Show Management</h1>");

            out.println("<a class='add' href='" + context
                    + "/ShowServlet'>+ Add New Show</a>");

            out.println("</div>");

            String message = request.getParameter("message");

            if ("added".equals(message)) {
                out.println("<div class='message'>Show added successfully.</div>");
            } else if ("updated".equals(message)) {
                out.println("<div class='message'>Show updated successfully.</div>");
            } else if ("deleted".equals(message)) {
                out.println("<div class='message'>Show deleted successfully.</div>");
            }

            out.println("<div class='table-box'>");

            out.println("<table>");

            out.println("<thead><tr>");
            out.println("<th>Show ID</th>");
            out.println("<th>Movie ID</th>");
            out.println("<th>Theatre ID</th>");
            out.println("<th>Show Date</th>");
            out.println("<th>Show Time</th>");
            out.println("<th>Ticket Price</th>");
            out.println("<th>Actions</th>");
            out.println("</tr></thead>");

            out.println("<tbody>");

            if (shows == null || shows.isEmpty()) {

                out.println("<tr><td colspan='7' class='empty'>");
                out.println("No shows found. Add a show to get started.");
                out.println("</td></tr>");

            } else {

                for (Show show : shows) {

                    int showId = show.getShowId();

                    out.println("<tr>");

                    out.println("<td>" + showId + "</td>");

                    out.println("<td>" + show.getMovieId() + "</td>");

                    out.println("<td>" + show.getTheatreId() + "</td>");

                    out.println("<td>" + show.getShowDate() + "</td>");

                    out.println("<td>" + show.getShowTime() + "</td>");

                    out.println("<td>₹" + show.getTicketPrice() + "</td>");

                    out.println("<td class='actions'>");

                    out.println("<a class='edit' href='"
                            + context
                            + "/EditShowServlet?id="
                            + showId
                            + "'>Edit</a> ");

                    out.println("<a class='delete' href='"
                            + context
                            + "/DeleteShowServlet?id="
                            + showId
                            + "'>Delete</a>");

                    out.println("</td>");

                    out.println("</tr>");
                }
            }

            out.println("</tbody>");
            out.println("</table>");

            out.println("</div>");

            out.println("<p class='count'>Total shows: "
                    + (shows == null ? 0 : shows.size())
                    + "</p>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (IOException e) {

            log("Error while displaying shows", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load shows. Please check the database connection."
            );
        }
    }
}
