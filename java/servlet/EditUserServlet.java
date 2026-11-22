
package servlet;

import dao.UserDAO;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    // GET - Display User Details
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        if (session == null || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(contextPath + "/admin-login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userDAO.getUserById(userId);

            if (user == null) {
                response.sendRedirect(contextPath + "/UserListServlet?error=notfound");
                return;
            }

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Edit User - MovieBook</title>");

            out.println("<style>");
            out.println("*{box-sizing:border-box;margin:0;padding:0;}");
            out.println("body{font-family:Arial,sans-serif;background:#090711;color:white;min-height:100vh;}");
            out.println(".navbar{background:#151020;padding:18px 5%;display:flex;justify-content:space-between;align-items:center;gap:15px;flex-wrap:wrap;border-bottom:1px solid #302342;}");
            out.println(".brand{font-size:23px;font-weight:bold;color:#c084fc;text-decoration:none;}");
            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");
            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");
            out.println(".nav-links a:hover{color:#c084fc;}");
            out.println(".container{width:92%;max-width:520px;margin:45px auto;background:#151020;border:1px solid #302342;border-radius:14px;padding:30px;box-shadow:0 15px 40px #0005;}");
            out.println("h1{text-align:center;color:#c084fc;margin-bottom:25px;}");
            out.println("label{display:block;margin:16px 0 7px;color:#ddd;font-size:14px;}");
            out.println("input{width:100%;padding:12px;border:1px solid #49345f;border-radius:7px;background:#0d0a16;color:white;outline:none;}");
            out.println("input:focus{border-color:#c084fc;}");
            out.println(".submit{margin-top:25px;background:#9333ea;border:0;font-weight:bold;cursor:pointer;}");
            out.println(".submit:hover{background:#7e22ce;}");
            out.println(".back{display:block;text-align:center;margin-top:20px;color:#c084fc;text-decoration:none;}");
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
            out.println("<h1>Edit User</h1>");

            out.println("<form action='" + contextPath + "/EditUserServlet' method='post'>");

            out.println("<input type='hidden' name='userId' value='" + user.getUserId() + "'>");

            out.println("<label>Name</label>");
            out.println("<input type='text' name='name' value='" + escape(user.getName()) + "' required>");

            out.println("<label>Email</label>");
            out.println("<input type='email' name='email' value='" + escape(user.getEmail()) + "' required>");

            out.println("<label>New Password</label>");
            out.println("<input type='password' name='password' placeholder='Leave blank to keep current password'>");

            out.println("<input class='submit' type='submit' value='Update User'>");
            out.println("</form>");

            out.println("<a class='back' href='" + contextPath + "/UserListServlet'>Back to User List</a>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {
            response.sendRedirect(contextPath + "/UserListServlet?error=invalid");
        } catch (Exception e) {
            throw new ServletException("Unable to load user details.", e);
        }
    }

    // POST - Update User
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();

        if (session == null || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(contextPath + "/admin-login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (name == null || email == null
                    || name.trim().isEmpty()
                    || email.trim().isEmpty()) {
                response.sendRedirect(contextPath + "/EditUserServlet?id="
                        + userId + "&error=empty");
                return;
            }

            User user = userDAO.getUserById(userId);

            if (user == null) {
                response.sendRedirect(contextPath + "/UserListServlet?error=notfound");
                return;
            }

            user.setName(name.trim());
            user.setEmail(email.trim());

            // Keep the existing password when the field is left blank.
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(password);
            }

            boolean result = userDAO.updateUser(user);

            if (result) {
                response.sendRedirect(contextPath + "/UserListServlet?message=updated");
            } else {
                response.sendRedirect(contextPath + "/EditUserServlet?id="
                        + userId + "&error=failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(contextPath + "/UserListServlet?error=invalid");
        } catch (Exception e) {
            throw new ServletException("Unable to update user.", e);
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
