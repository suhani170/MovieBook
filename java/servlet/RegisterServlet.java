
package servlet;

import dao.UserDAO;
import model.User;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Customer registration with server-side validation.
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[\\p{L}\\p{M}]+(?:[ '\\u2019-][\\p{L}\\p{M}]+)*$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@"
                    + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
                    + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$");

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        name = name == null ? "" : name.trim();
        email = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        password = password == null ? "" : password;
        confirmPassword = confirmPassword == null ? "" : confirmPassword;

        String error = null;

        // Check required fields.
        if (name.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {

            error = "Please fill in all the fields.";

        // Validate customer name.
        } else if (name.length() < 2 || name.length() > 100
                || !NAME_PATTERN.matcher(name).matches()) {

            error = "Enter a valid name using letters, spaces, apostrophes or hyphens.";

        // Validate email.
        } else if (email.length() > 100
                || !EMAIL_PATTERN.matcher(email).matches()
                || email.contains("..")) {

            error = "Please enter a valid email address.";

        // Validate password length.
        } else if (password.length() < 8 || password.length() > 64) {

            error = "Password must contain between 8 and 64 characters.";

        // Require uppercase, lowercase, number and special character.
        } else if (!password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*[0-9].*")
                || !password.matches(".*[^A-Za-z0-9].*")) {

            error = "Password must include uppercase, lowercase, a number and a special character.";

        // Check password confirmation.
        } else if (!password.equals(confirmPassword)) {

            error = "Password and confirm password do not match.";

        // Prevent duplicate registrations.
        } else if (userDAO.emailExists(email)) {

            error = "This email is already registered. Please login.";

        }

        // Save the customer only after all validations pass.
        if (error == null) {

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            if (userDAO.addUser(user)) {
                response.sendRedirect(
                        request.getContextPath() + "/customer-login.jsp?registered=1");
                return;
            }

            error = "Registration failed. Please try again.";
        }

        request.setAttribute("error", error);
        request.setAttribute("name", name);
        request.setAttribute("email", email);

        request.getRequestDispatcher("/register.jsp")
                .forward(request, response);
    }
}
