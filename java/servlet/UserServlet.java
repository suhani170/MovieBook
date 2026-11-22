
package servlet;

import dao.UserDAO;
import model.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    // CREATE - Add User
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

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || email == null || password == null
                || name.trim().isEmpty()
                || email.trim().isEmpty()
                || password.trim().isEmpty()) {

            response.sendRedirect(contextPath + "/user.jsp?error=empty");
            return;
        }

        try {
            User user = new User();

            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPassword(password);

            boolean result = userDAO.addUser(user);

            if (result) {
                response.sendRedirect(
                        contextPath + "/UserListServlet?message=added");
            } else {
                response.sendRedirect(
                        contextPath + "/user.jsp?error=failed");
            }

        } catch (Exception e) {
            throw new ServletException("Unable to add user.", e);
        }
    }

    // GET - Open Add User page
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin-login.jsp");
            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/user.jsp");
    }
}
