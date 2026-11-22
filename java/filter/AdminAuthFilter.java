
package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
    "/admin-dashboard.jsp",

    "/MovieListServlet", "/MovieServlet",
    "/EditMovieServlet", "/DeleteMovieServlet",

    "/TheatreListServlet", "/TheatreServlet",
    "/EditTheatreServlet", "/DeleteTheatreServlet",

    "/ShowListServlet", "/ShowServlet",
    "/EditShowServlet", "/DeleteShowServlet",

    "/UserListServlet", "/UserServlet",
    "/EditUserServlet", "/DeleteUserServlet",

    "/BookingListServlet", "/BookingServlet",
    "/EditBookingServlet", "/DeleteBookingServlet",

    "/theatre.jsp", "/user.jsp", "/booking.jsp"
})
public class AdminAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Cache-Control",
                "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);

        Object adminUsername = (session != null)
                ? session.getAttribute("adminUsername")
                : null;

        boolean isAdmin = adminUsername instanceof String
                && !((String) adminUsername).trim().isEmpty();

        if (isAdmin) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/admin-login.jsp"
            );
        }
    }
}
