
package servlet;

import dao.AdminDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {

    private AdminDAO adminDAO;

    @Override
    public void init() throws ServletException {
        adminDAO = new AdminDAO();
    }

    private void preventCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, private");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        preventCaching(response);

        response.sendRedirect(
                request.getContextPath() + "/admin-login.jsp"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        preventCaching(response);

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String context = request.getContextPath();

        if (username == null || password == null
                || username.trim().isEmpty()
                || password.isEmpty()) {

            response.sendRedirect(
                    context + "/admin-login.jsp?error=2"
            );
            return;
        }

        boolean validAdmin = adminDAO.login(
                username.trim(), password
        );

        if (validAdmin) {

            // Invalidate the previous session for security.
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create a fresh admin session.
            HttpSession session = request.getSession(true);

            session.setAttribute("adminUsername", username.trim());
            session.setAttribute("adminLoggedIn", true);

            // Session expires after 30 minutes of inactivity.
            session.setMaxInactiveInterval(1800);

            response.sendRedirect(
                    context + "/admin-dashboard.jsp"
            );

        } else {

            response.sendRedirect(
                    context + "/admin-login.jsp?error=1"
            );
        }
    }
}
