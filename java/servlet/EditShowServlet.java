
package servlet;

import dao.ShowDAO;
import dao.MovieDAO;
import dao.TheatreDAO;
import model.Show;
import model.Movie;
import model.Theatre;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/EditShowServlet")
public class EditShowServlet extends HttpServlet {

    private ShowDAO showDAO;
    private MovieDAO movieDAO;
    private TheatreDAO theatreDAO;

    @Override
    public void init() throws ServletException {
        showDAO = new ShowDAO();
        movieDAO = new MovieDAO();
        theatreDAO = new TheatreDAO();
    }

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("adminUsername") != null;
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

    private String formatTime(String time) {
        if (time == null) {
            return "";
        }

        return time.length() >= 5 ? time.substring(0, 5) : time;
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

        out.println(".container{width:92%;max-width:650px;margin:40px auto;}");

        out.println(".card{background:#160e27;border:1px solid #38264c;border-radius:15px;padding:30px;box-shadow:0 12px 35px rgba(0,0,0,.3);}");

        out.println("h1{text-align:center;color:#d6baff;margin-bottom:10px;font-size:28px;}");

        out.println(".subtitle{text-align:center;color:#aaa;margin-bottom:28px;font-size:14px;}");

        out.println("label{display:block;margin:18px 0 8px;color:#ddd;font-size:14px;}");

        out.println("input,select{width:100%;padding:13px;background:#0d0917;border:1px solid #49345f;border-radius:8px;color:white;font-size:14px;outline:none;}");

        out.println("input:focus,select:focus{border-color:#a879f5;}");

        out.println("select option{background:#160e27;color:white;}");

        out.println(".submit{width:100%;margin-top:25px;background:#8b5cf6;border:none;border-radius:8px;padding:14px;color:white;font-size:15px;font-weight:bold;cursor:pointer;}");

        out.println(".submit:hover{background:#7543d8;}");

        out.println(".back{display:block;text-align:center;margin-top:20px;color:#c69cff;text-decoration:none;font-size:14px;}");

        out.println(".error{background:#54212c;color:#ffb4b4;padding:12px;border-radius:7px;margin-bottom:15px;font-size:14px;}");

        out.println("@media(max-width:600px){.container{margin:25px auto;}.card{padding:22px;}nav{padding:15px;}}");

        out.println("</style>");
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

            int showId = Integer.parseInt(request.getParameter("id"));

            Show show = showDAO.getShowById(showId);

            if (show == null) {
                response.sendRedirect(context + "/ShowListServlet");
                return;
            }

            List<Movie> movies = movieDAO.getAllMovies();
            List<Theatre> theatres = theatreDAO.getAllTheatres();

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Edit Show - MovieBook</title>");

            writeStyles(out);

            out.println("</head>");
            out.println("<body>");

            writeNavigation(out, context);

            out.println("<main class='container'>");
            out.println("<div class='card'>");

            out.println("<h1>Edit Movie Show</h1>");
            out.println("<p class='subtitle'>Update the movie show details</p>");

            String error = request.getParameter("error");

            if ("invalid".equals(error)) {
                out.println("<div class='error'>Please enter valid show details and a positive ticket price.</div>");
            } else if ("failed".equals(error)) {
                out.println("<div class='error'>Unable to update the show. Please try again.</div>");
            }

            out.println("<form method='post' action='" + context
                    + "/EditShowServlet'>");

            out.println("<input type='hidden' name='showId' value='"
                    + show.getShowId() + "'>");

            out.println("<label>Select Movie</label>");
            out.println("<select name='movieId' required>");

            for (Movie movie : movies) {

                out.println("<option value='" + movie.getMovieId() + "'"
                        + (movie.getMovieId() == show.getMovieId()
                        ? " selected" : "")
                        + ">" + escape(movie.getTitle()) + "</option>");
            }

            out.println("</select>");

            out.println("<label>Select Theatre</label>");
            out.println("<select name='theatreId' required>");

            for (Theatre theatre : theatres) {

                out.println("<option value='" + theatre.getTheatreId() + "'"
                        + (theatre.getTheatreId() == show.getTheatreId()
                        ? " selected" : "")
                        + ">" + escape(theatre.getTheatreName())
                        + " - " + escape(theatre.getLocation())
                        + "</option>");
            }

            out.println("</select>");

            out.println("<label>Show Date</label>");
            out.println("<input type='date' name='showDate' value='"
                    + escape(show.getShowDate()) + "' required>");

            out.println("<label>Show Time</label>");
            out.println("<input type='time' name='showTime' value='"
                    + escape(formatTime(show.getShowTime())) + "' required>");

            out.println("<label>Ticket Price (₹)</label>");
            out.println("<input type='number' name='ticketPrice' min='1' "
                    + "step='0.01' value='" + show.getTicketPrice()
                    + "' required>");

            out.println("<button class='submit' type='submit'>Update Show</button>");

            out.println("</form>");

            out.println("<a class='back' href='" + context
                    + "/ShowListServlet'>← Back to Show Management</a>");

            out.println("</div>");
            out.println("</main>");

            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {

            response.sendRedirect(context + "/ShowListServlet");

        } catch (Exception e) {

            log("Error while loading edit show form", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load show details."
            );
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

            int showId = Integer.parseInt(request.getParameter("showId"));
            int movieId = Integer.parseInt(request.getParameter("movieId"));
            int theatreId = Integer.parseInt(request.getParameter("theatreId"));

            String showDate = request.getParameter("showDate");
            String showTime = request.getParameter("showTime");

            double ticketPrice = Double.parseDouble(
                    request.getParameter("ticketPrice"));

            if (showId <= 0 || movieId <= 0 || theatreId <= 0
                    || showDate == null || showDate.trim().isEmpty()
                    || showTime == null || showTime.trim().isEmpty()
                    || ticketPrice <= 0 || !Double.isFinite(ticketPrice)) {

                response.sendRedirect(context
                        + "/EditShowServlet?id=" + showId + "&error=invalid");
                return;
            }

            LocalDate.parse(showDate);
            LocalTime time = LocalTime.parse(showTime);

            Show show = new Show();

            show.setShowId(showId);
            show.setMovieId(movieId);
            show.setTheatreId(theatreId);
            show.setShowDate(showDate);
            show.setShowTime(time.toString());
            show.setTicketPrice(ticketPrice);

            boolean result = showDAO.updateShow(show);

            if (result) {
                response.sendRedirect(context
                        + "/ShowListServlet?message=updated");
            } else {
                response.sendRedirect(context
                        + "/EditShowServlet?id=" + showId + "&error=failed");
            }

        } catch (NumberFormatException | java.time.DateTimeException e) {

            response.sendRedirect(context + "/ShowListServlet");

        } catch (IOException e) {

            log("Error while updating show", e);

            response.sendRedirect(context + "/ShowListServlet");
        }
    }
}
