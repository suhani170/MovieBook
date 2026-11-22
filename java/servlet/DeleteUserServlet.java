
package servlet;

import dao.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    // GET - Display deletion confirmation
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getContextPath();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(contextPath + "/admin-login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Delete User - MovieBook</title>");

            out.println("<style>");
            out.println("*{box-sizing:border-box;margin:0;padding:0;}");
            out.println("body{font-family:Arial,sans-serif;background:#090711;color:white;min-height:100vh;}");
            out.println(".navbar{background:#151020;padding:18px 5%;display:flex;justify-content:space-between;align-items:center;gap:15px;flex-wrap:wrap;border-bottom:1px solid #302342;}");
            out.println(".brand{font-size:23px;font-weight:bold;color:#c084fc;text-decoration:none;}");
            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");
            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");
            out.println(".nav-links a:hover{color:#c084fc;}");
            out.println(".container{width:92%;max-width:500px;margin:70px auto;background:#151020;border:1px solid #302342;border-radius:14px;padding:35px;text-align:center;}");
            out.println("h1{color:#fca5a5;margin-bottom:20px;}");
            out.println("p{color:#ccc;line-height:1.7;margin-bottom:25px;}");
            out.println(".button{display:inline-block;padding:12px 20px;border:0;border-radius:7px;text-decoration:none;font-weight:bold;cursor:pointer;margin:5px;}");
            out.println(".delete{background:#dc2626;color:white;}");
            out.println(".delete:hover{background:#b91c1c;}");
            out.println(".cancel{background:#39235b;color:#e9d5ff;}");
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
            out.println("<a href='" + contextPath + "/UserListServlet'>Users</a>");
            out.println("<a href='" + contextPath + "/BookingListServlet'>Bookings</a>");
            out.println("<a href='" + contextPath + "/LogoutServlet'>Logout</a>");
            out.println("</div>");
            out.println("</nav>");

            out.println("<div class='container'>");
            out.println("<h1>Delete User?</h1>");
            out.println("<p>Are you sure you want to delete user ID <strong>"
                    + userId + "</strong>? This action cannot be undone.</p>");

            out.println("<form action='" + contextPath + "/DeleteUserServlet' method='post'>");
            out.println("<input type='hidden' name='id' value='" + userId + "'>");
            out.println("<button class='button delete' type='submit'>Yes, Delete User</button>");
            out.println("<a class='button cancel' href='" + contextPath
                    + "/UserListServlet'>Cancel</a>");
            out.println("</form>");

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {
            response.sendRedirect(contextPath + "/UserListServlet?error=invalid");
        }
    }

    // POST - Delete User after confirmation
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getContextPath();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(contextPath + "/admin-login.jsp");
            return;
        }

        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            boolean result = userDAO.deleteUser(userId);

            if (result) {
                response.sendRedirect(
                        contextPath + "/UserListServlet?message=deleted");
            } else {
                response.sendRedirect(
                        contextPath + "/UserListServlet?error=deletefailed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(contextPath + "/UserListServlet?error=invalid");
        } catch (IOException e) {
            throw new ServletException("Unable to delete user.", e);
        }
    }
}
