
package servlet;

import dao.TheatreDAO;
import model.Theatre;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/TheatreListServlet")
public class TheatreListServlet extends HttpServlet {

    private TheatreDAO theatreDAO;

    @Override
    public void init() throws ServletException {
        theatreDAO = new TheatreDAO();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("adminUsername") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/admin-login.jsp"
            );
            return;
        }

        String context = request.getContextPath();

        try {

            List<Theatre> theatres = theatreDAO.getAllTheatres();

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");

            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Theatre Management - MovieBook</title>");

            out.println("<style>");

            out.println("*{margin:0;padding:0;box-sizing:border-box;}");

            out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:#fff;min-height:100vh;}");

            out.println("nav{background:#160e27;padding:17px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");

            out.println(".logo{font-size:23px;font-weight:bold;color:#b78aff;text-decoration:none;}");

            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");

            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;transition:.2s;}");

            out.println(".nav-links a:hover,.nav-links a.active{color:#c69cff;}");

            out.println(".container{width:92%;max-width:1150px;margin:40px auto;}");

            out.println(".heading{display:flex;justify-content:space-between;align-items:center;gap:15px;flex-wrap:wrap;margin-bottom:25px;}");

            out.println("h1{font-size:29px;color:#d6baff;}");

            out.println(".add{background:#8b5cf6;color:white;padding:12px 18px;border-radius:8px;text-decoration:none;font-weight:bold;font-size:14px;}");

            out.println(".add:hover{background:#7543d8;}");

            out.println(".table-box{background:#160e27;border:1px solid #38264c;border-radius:12px;overflow-x:auto;}");

            out.println("table{width:100%;border-collapse:collapse;min-width:650px;}");

            out.println("th,td{padding:16px;text-align:left;border-bottom:1px solid #302044;}");

            out.println("th{background:#211433;color:#cba8ff;font-size:13px;text-transform:uppercase;letter-spacing:.5px;}");

            out.println("td{color:#ddd;font-size:14px;}");

            out.println("tr:last-child td{border-bottom:none;}");

            out.println("tr:hover td{background:#20152f;}");

            out.println(".actions{white-space:nowrap;}");

            out.println(".edit,.delete{display:inline-block;padding:8px 12px;border-radius:6px;text-decoration:none;font-size:12px;font-weight:bold;}");

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

            out.println("<a class='active' href='" + context
                    + "/TheatreListServlet'>Theatres</a>");

            out.println("<a href='" + context
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

            out.println("<h1>Theatre Management</h1>");

            out.println("<a class='add' href='" + context
                    + "/theatre.jsp'>+ Add New Theatre</a>");

            out.println("</div>");

            String message = request.getParameter("message");

            if ("added".equals(message)) {
                out.println("<div class='message'>Theatre added successfully.</div>");
            } else if ("updated".equals(message)) {
                out.println("<div class='message'>Theatre updated successfully.</div>");
            } else if ("deleted".equals(message)) {
                out.println("<div class='message'>Theatre deleted successfully.</div>");
            }

            out.println("<div class='table-box'>");
            out.println("<table>");

            out.println("<thead><tr>");
            out.println("<th>ID</th>");
            out.println("<th>Theatre Name</th>");
            out.println("<th>Location</th>");
            out.println("<th>Actions</th>");
            out.println("</tr></thead>");

            out.println("<tbody>");

            if (theatres == null || theatres.isEmpty()) {

                out.println("<tr><td colspan='4' class='empty'>");
                out.println("No theatres found. Add a theatre to get started.");
                out.println("</td></tr>");

            } else {

                for (Theatre theatre : theatres) {

                    int theatreId = theatre.getTheatreId();

                    out.println("<tr>");

                    out.println("<td>" + theatreId + "</td>");

                    out.println("<td>"
                            + escape(theatre.getTheatreName())
                            + "</td>");

                    out.println("<td>"
                            + escape(theatre.getLocation())
                            + "</td>");

                    out.println("<td class='actions'>");

                    out.println("<a class='edit' href='"
                            + context
                            + "/EditTheatreServlet?id="
                            + theatreId
                            + "'>Edit</a> ");

                    out.println("<a class='delete' href='"
                            + context
                            + "/DeleteTheatreServlet?id="
                            + theatreId
                            + "'>Delete</a>");

                    out.println("</td>");

                    out.println("</tr>");
                }
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

            out.println("<p class='count'>Total theatres: "
                    + (theatres == null ? 0 : theatres.size())
                    + "</p>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (IOException e) {

            log("Error while displaying theatres", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load theatres. Please check the database connection."
            );
        }
    }
}
