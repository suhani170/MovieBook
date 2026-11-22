
package util;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthUtil {

    // Get the logged-in customer's ID
    public static Integer getUserId(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object userId = session.getAttribute("userId");

        if (userId instanceof Integer) {
            return (Integer) userId;
        }

        return null;
    }

    // Redirect customers to login and preserve a safe return destination
    public static void redirectToLogin(HttpServletRequest request,
                                       HttpServletResponse response,
                                       String returnTo)
            throws IOException {

        String safeTarget = safeRedirect(returnTo);

        String encodedTarget = URLEncoder.encode(
                safeTarget,
                StandardCharsets.UTF_8
        );

        response.sendRedirect(
                request.getContextPath()
                + "/customer-login.jsp?redirect="
                + encodedTarget
        );
    }

    // Allow only simple relative page names and safe query parameters
    public static String safeRedirect(String target) {

        if (target == null || target.trim().isEmpty()) {
            return "index.jsp";
        }

        String safePattern =
                "[A-Za-z0-9_-]+(\\.jsp)?(\\?[A-Za-z0-9_=&-]*)?";

        if (target.matches(safePattern)) {
            return target;
        }

        return "index.jsp";
    }
}
