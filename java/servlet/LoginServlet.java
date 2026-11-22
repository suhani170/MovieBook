package servlet;

import dao.UserDAO;
import model.User;
import util.AuthUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Customer login (public). Stores userId + userName in the session.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("customer-login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        email = (email == null) ? "" : email.trim().toLowerCase();
        password = (password == null) ? "" : password;

        User user = userDAO.login(email, password);

        if (user == null) {

            request.setAttribute("error", "Invalid email or password.");
            request.setAttribute("email", email);
            request.setAttribute("redirect", redirect);

            request.getRequestDispatcher("/customer-login.jsp")
                   .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        // new session id after login (protects against session fixation)
        request.changeSessionId();

        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());

        response.sendRedirect(AuthUtil.safeRedirect(redirect));
    }
}
