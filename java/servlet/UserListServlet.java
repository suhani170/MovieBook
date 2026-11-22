
package servlet;

import dao.UserDAO;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserListServlet")
public class UserListServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        String contextPath = request.getContextPath();

        try (PrintWriter out = response.getWriter()) {

            List<User> users = userDAO.getAllUsers();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>User Management - MovieBook</title>");

            out.println("<style>");
            out.println("*{box-sizing:border-box;margin:0;padding:0;}");
            out.println("body{font-family:Arial,sans-serif;background:#090711;color:#fff;min-height:100vh;}");
            out.println(".navbar{background:#151020;padding:18px 5%;display:flex;justify-content:space-between;align-items:center;gap:15px;flex-wrap:wrap;border-bottom:1px solid #302342;}");
            out.println(".brand{color:#c084fc;font-size:23px;font-weight:bold;text-decoration:none;}");
            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");
            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");
            out.println(".nav-links a:hover,.active{color:#c084fc!important;}");
            out.println(".container{width:92%;max-width:1200px;margin:40px auto;}");
            out.println(".heading{display:flex;justify-content:space-between;align-items:center;gap:15px;flex-wrap:wrap;margin-bottom:25px;}");
            out.println("h1{font-size:30px;color:#fff;}");
            out.println(".count{color:#bbb;margin-top:8px;font-size:14px;}");
            out.println(".table-wrap{overflow-x:auto;background:#151020;border:1px solid #302342;border-radius:12px;}");
            out.println("table{width:100%;border-collapse:collapse;min-width:650px;}");
            out.println("th,td{padding:16px;text-align:left;border-bottom:1px solid #302342;}");
            out.println("th{background:#21172f;color:#c084fc;font-size:13px;}");
            out.println("td{color:#ddd;font-size:14px;}");
            out.println("tr:hover td{background:#1d1529;}");
            out.println(".action{display:inline-block;padding:8px 12px;border-radius:6px;text-decoration:none;font-size:13px;font-weight:bold;margin:2px;}");
            out.println(".edit{background:#39235b;color:#d8b4fe;}");
            out.println(".delete{background:#4a202b;color:#fecaca;}");
            out.println(".empty{text-align:center;padding:35px;color:#aaa;}");
            out.println(".back{display:inline-block;margin-top:22px;color:#c084fc;text-decoration:none;}");
            out.println("@media(max-width:600px){.container{margin:25px auto;}h1{font-size:24px;}}");
            out.println("</style>");
            out.println("</head>");

            out.println("<body>");

            out.println("<nav class='navbar'>");
            out.println("<a class='brand' href='" + contextPath + "/admin-dashboard.jsp'>MovieBook Admin</a>");
            out.println("<div class='nav-links'>");
            out.println("<a href='" + contextPath + "/admin-dashboard.jsp'>Dashboard</a>");
            out.println("<a href='" + contextPath + "/MovieListServlet'>Movies</a>");
            out.println("<a href='" + contextPath + "/TheatreListServlet'>Theatres</a>");
            out.println("<a href='" + contextPath + "/ShowListServlet'>Shows</a>");
            out.println("<a class='active' href='" + contextPath + "/UserListServlet'>Users</a>");
            out.println("<a href='" + contextPath + "/BookingListServlet'>Bookings</a>");
            out.println("<a href='" + contextPath + "/LogoutServlet'>Logout</a>");
            out.println("</div>");
            out.println("</nav>");

            out.println("<main class='container'>");
            out.println("<div class='heading'>");
            out.println("<div>");
            out.println("<h1>User Management</h1>");
            out.println("<p class='count'>Registered customers: " + users.size() + "</p>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div class='table-wrap'>");
            out.println("<table>");
            out.println("<thead><tr>");
            out.println("<th>User ID</th>");
            out.println("<th>Name</th>");
            out.println("<th>Email</th>");
            out.println("<th>Actions</th>");
            out.println("</tr></thead>");
            out.println("<tbody>");

            if (users.isEmpty()) {
                out.println("<tr><td colspan='4' class='empty'>No registered users found.</td></tr>");
            } else {
                for (User user : users) {
                    int userId = user.getUserId();

                    out.println("<tr>");
                    out.println("<td>" + userId + "</td>");
                    out.println("<td>" + escape(user.getName()) + "</td>");
                    out.println("<td>" + escape(user.getEmail()) + "</td>");
                    out.println("<td>");

                    out.println("<a class='action edit' href='" + contextPath
                            + "/EditUserServlet?id=" + userId + "'>Edit</a>");

                    out.println("<a class='action delete' href='" + contextPath
                            + "/DeleteUserServlet?id=" + userId + "'>Delete</a>");

                    out.println("</td>");
                    out.println("</tr>");
                }
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

            out.println("<a class='back' href='" + contextPath
                    + "/admin-dashboard.jsp'>Back to Dashboard</a>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            throw new ServletException("Unable to display users.", e);
        }
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
}
