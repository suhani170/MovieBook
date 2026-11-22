
package servlet;

import dao.ShowDAO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DeleteShowServlet")
public class DeleteShowServlet extends HttpServlet {

    private ShowDAO showDAO;

    @Override
    public void init() throws ServletException {
        showDAO = new ShowDAO();
    }

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("adminUsername") != null;
    }

    private void writeNavigation(PrintWriter out, String context) {

        out.println("<nav>");
        out.println("<a class='logo' href='" + context
                + "/admin-dashboard.jsp'>MovieBook Admin</a>");

        out.println("<div class='nav-links'>");

        out.println("<a href='" + context
                + "/admin-dashboard.jsp'>Dashboard</a>");

        out.println("<a href='" + context
                + "/MovieListServlet'>Movies</a>");

        out.println("<a href='" + context
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
    }

    private void writeStyles(PrintWriter out) {

        out.println("<style>");

        out.println("*{margin:0;padding:0;box-sizing:border-box;}");

        out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:white;min-height:100vh;}");

        out.println("nav{background:#160e27;padding:17px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");

        out.println(".logo{font-size:23px;font-weight:bold;color:#b78aff;text-decoration:none;}");

        out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");

        out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");

        out.println(".nav-links a:hover{color:#c69cff;}");

        out.println(".container{width:92%;max-width:550px;margin:70px auto;}");

        out.println(".card{background:#160e27;border:1px solid #542d43;border-radius:15px;padding:35px;text-align:center;box-shadow:0 12px 35px rgba(0,0,0,.3);}");

        out.println(".symbol{font-size:48px;margin-bottom:18px;}");

        out.println("h1{color:#ffaaaa;font-size:26px;margin-bottom:15px;}");

        out.println("p{color:#ccc;line-height:1.7;margin-bottom:25px;font-size:14px;}");

        out.println(".actions{display:flex;justify-content:center;gap:12px;flex-wrap:wrap;}");

        out.println(".btn{border:0;border-radius:8px;padding:12px 20px;text-decoration:none;font-size:14px;font-weight:bold;cursor:pointer;}");

        out.println(".confirm{background:#a83248;color:white;}");

        out.println(".confirm:hover{background:#c13c55;}");

        out.println(".cancel{background:#49317a;color:white;}");

        out.println(".cancel:hover{background:#6545a0;}");

        out.println(".error{color:#ffaaaa;background:#54212c;padding:14px;border-radius:8px;margin-bottom:20px;}");

        out.println("@media(max-width:600px){.container{margin:35px auto;}.card{padding:25px;}nav{padding:15px;}}");

        out.println("</style>");
    }

    private void showPage(HttpServletRequest request,
                          HttpServletResponse response,
                          int showId,
                          boolean failed)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        String context = request.getContextPath();

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Delete Show - MovieBook</title>");

        writeStyles(out);

        out.println("</head>");
        out.println("<body>");

        writeNavigation(out, context);

        out.println("<main class='container'>");
        out.println("<div class='card'>");

        if (failed) {

            out.println("<div class='symbol'>&#9888;</div>");
            out.println("<h1>Show Deletion Failed</h1>");

            out.println("<div class='error'>This show could not be deleted. "
                    + "It may have existing bookings linked to it.</div>");

            out.println("<p>Existing booking records are protected. "
                    + "Check the related bookings before trying again.</p>");

            out.println("<a class='btn cancel' href='" + context
                    + "/ShowListServlet'>Back to Show Management</a>");

        } else {

            out.println("<div class='symbol'>&#128465;</div>");
            out.println("<h1>Delete This Show?</h1>");

            out.println("<p>Are you sure you want to delete show ID <strong>"
                    + showId
                    + "</strong>? This action cannot be undone.</p>");

            out.println("<div class='actions'>");

            out.println("<form method='post' action='" + context
                    + "/DeleteShowServlet'>");

            out.println("<input type='hidden' name='id' value='"
                    + showId + "'>");

            out.println("<button class='btn confirm' type='submit'>"
                    + "Yes, Delete Show</button>");

            out.println("</form>");

            out.println("<a class='btn cancel' href='" + context
                    + "/ShowListServlet'>Cancel</a>");

            out.println("</div>");
        }

        out.println("</div>");
        out.println("</main>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        try {

            int showId = Integer.parseInt(request.getParameter("id"));

            if (showId <= 0) {
                response.sendRedirect(context + "/ShowListServlet");
                return;
            }

            showPage(request, response, showId, false);

        } catch (NumberFormatException e) {

            response.sendRedirect(context + "/ShowListServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        try {

            int showId = Integer.parseInt(request.getParameter("id"));

            if (showId <= 0) {
                response.sendRedirect(context + "/ShowListServlet");
                return;
            }

            boolean result = showDAO.deleteShow(showId);

            if (result) {

                response.sendRedirect(context
                        + "/ShowListServlet?message=deleted");

            } else {

                showPage(request, response, showId, true);
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(context + "/ShowListServlet");

        } catch (IOException e) {

            log("Error while deleting show", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to delete the show."
            );
        }
    }
}
