
package servlet;

import dao.TheatreDAO;
import model.Theatre;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/EditTheatreServlet")
public class EditTheatreServlet extends HttpServlet {

    private TheatreDAO theatreDAO;

    @Override
    public void init() throws ServletException {
        theatreDAO = new TheatreDAO();
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

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("adminUsername") != null;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        try {

            int theatreId = Integer.parseInt(
                    request.getParameter("id"));

            if (theatreId <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid theatre ID."
                );
                return;
            }

            Theatre theatre = theatreDAO.getTheatreById(theatreId);

            if (theatre == null) {
                response.sendRedirect(
                        context + "/TheatreListServlet"
                );
                return;
            }

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Edit Theatre - MovieBook</title>");

            out.println("<style>");

            out.println("*{margin:0;padding:0;box-sizing:border-box;}");

            out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:#fff;min-height:100vh;}");

            out.println("nav{background:#160e27;padding:17px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");

            out.println(".logo{font-size:23px;font-weight:bold;color:#b78aff;text-decoration:none;}");

            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");

            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");

            out.println(".nav-links a:hover{color:#c69cff;}");

            out.println(".container{width:90%;max-width:550px;margin:55px auto;background:#160e27;padding:32px;border:1px solid #49345f;border-radius:14px;box-shadow:0 12px 35px rgba(0,0,0,.3);}");

            out.println("h1{text-align:center;color:#d6baff;margin-bottom:25px;font-size:27px;}");

            out.println("label{display:block;margin:18px 0 8px;color:#ddd;font-weight:bold;font-size:14px;}");

            out.println("input[type=text]{width:100%;padding:13px;border:1px solid #49345f;border-radius:7px;background:#0d0818;color:#fff;outline:none;font-size:14px;}");

            out.println("input[type=text]:focus{border-color:#a879ff;}");

            out.println(".submit{width:100%;margin-top:25px;padding:13px;background:#8b5cf6;color:white;border:0;border-radius:7px;font-weight:bold;font-size:15px;cursor:pointer;}");

            out.println(".submit:hover{background:#7543d8;}");

            out.println(".back{display:block;text-align:center;margin-top:20px;color:#c69cff;text-decoration:none;font-size:14px;}");

            out.println(".back:hover{color:#fff;}");

            out.println("@media(max-width:600px){nav{padding:15px;}.container{margin:30px auto;padding:23px;}}");

            out.println("</style>");
            out.println("</head>");

            out.println("<body>");

            // Admin navigation
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

            out.println("<main class='container'>");

            out.println("<h1>Edit Theatre</h1>");

            out.println("<form action='" + context
                    + "/EditTheatreServlet' method='post'>");

            out.println("<input type='hidden' name='theatreId' value='"
                    + theatre.getTheatreId() + "'>");

            out.println("<label for='theatreName'>Theatre Name</label>");

            out.println("<input type='text' id='theatreName' name='theatreName' value='"
                    + escape(theatre.getTheatreName())
                    + "' maxlength='100' required>");

            out.println("<label for='location'>Location</label>");

            out.println("<input type='text' id='location' name='location' value='"
                    + escape(theatre.getLocation())
                    + "' maxlength='150' required>");

            out.println("<button class='submit' type='submit'>Update Theatre</button>");

            out.println("</form>");

            out.println("<a class='back' href='" + context
                    + "/TheatreListServlet'>Back to Theatre List</a>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid theatre ID."
            );

        } catch (Exception e) {

            log("Error while loading theatre for editing", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load theatre details."
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        try {

            int theatreId = Integer.parseInt(
                    request.getParameter("theatreId"));

            String theatreName = request.getParameter("theatreName");
            String location = request.getParameter("location");

            if (theatreId <= 0
                    || theatreName == null
                    || theatreName.trim().isEmpty()
                    || location == null
                    || location.trim().isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Please enter valid theatre details."
                );
                return;
            }

            Theatre theatre = new Theatre();

            theatre.setTheatreId(theatreId);
            theatre.setTheatreName(theatreName.trim());
            theatre.setLocation(location.trim());

            boolean result = theatreDAO.updateTheatre(theatre);

            if (result) {

                response.sendRedirect(
                        context + "/TheatreListServlet?message=updated"
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Theatre could not be updated."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid theatre ID."
            );

        } catch (IOException e) {

            log("Error while updating theatre", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while updating the theatre."
            );
        }
    }
}
