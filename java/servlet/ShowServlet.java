
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

@WebServlet("/ShowServlet")
public class ShowServlet extends HttpServlet {

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

            List<Movie> movies = movieDAO.getAllMovies();
            List<Theatre> theatres = theatreDAO.getAllTheatres();

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Add Show - MovieBook</title>");

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

            out.println(".empty{color:#ffb4b4;font-size:13px;margin-top:8px;}");

            out.println(".movie-preview{display:none;align-items:center;gap:18px;background:#0d0917;border:1px solid #49345f;border-radius:10px;padding:15px;margin-top:15px;}");

            out.println(".movie-preview img{width:105px;height:145px;object-fit:cover;border-radius:8px;background:#211633;}");

            out.println(".movie-info{flex:1;min-width:0;}");

            out.println(".movie-info h3{color:#d6baff;font-size:18px;margin-bottom:10px;}");

            out.println(".movie-info p{color:#bbb;font-size:13px;line-height:1.7;margin-bottom:5px;overflow-wrap:anywhere;}");

            out.println(".movie-info strong{color:#e9d5ff;}");

            out.println(".movie-description{margin-top:8px!important;}");

            out.println("@media(max-width:600px){.container{margin:25px auto;}.card{padding:22px;}nav{padding:15px;}.movie-preview{align-items:flex-start;}.movie-preview img{width:85px;height:120px;}}");

            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

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
            out.println("<div class='card'>");

            out.println("<h1>Add Movie Show</h1>");
            out.println("<p class='subtitle'>Schedule a movie at a theatre</p>");

            String error = request.getParameter("error");

            if ("empty".equals(error)) {
                out.println("<div class='error'>Please fill in all fields.</div>");
            } else if ("invalid".equals(error)) {
                out.println("<div class='error'>Please enter valid show details and a positive ticket price.</div>");
            } else if ("failed".equals(error)) {
                out.println("<div class='error'>Unable to add the show. Please check the selected movie, theatre and database.</div>");
            }

            out.println("<form method='post' action='" + context
                    + "/ShowServlet'>");

            out.println("<label>Select Movie</label>");

            out.println("<select name='movieId' id='movieId' required>");
            out.println("<option value=''>Choose a movie</option>");

            if (movies != null) {
                for (Movie movie : movies) {

                    out.println("<option value='" + movie.getMovieId()
                            + "' data-title='" + escape(movie.getTitle())
                            + "' data-genre='" + escape(movie.getGenre())
                            + "' data-language='" + escape(movie.getLanguage())
                            + "' data-duration='" + movie.getDuration()
                            + "' data-description='" + escape(movie.getDescription())
                            + "' data-poster='" + escape(movie.getPosterUrl())
                            + "'>" + escape(movie.getTitle()) + "</option>");
                }
            }

            out.println("</select>");

            if (movies == null || movies.isEmpty()) {
                out.println("<p class='empty'>No movies found. Add a movie first.</p>");
            }

            out.println("<div class='movie-preview' id='moviePreview'>");

            out.println("<img id='previewPoster' src='' alt='Movie Poster'>");

            out.println("<div class='movie-info'>");

            out.println("<h3 id='previewTitle'></h3>");

            out.println("<p><strong>Genre:</strong> <span id='previewGenre'></span></p>");

            out.println("<p><strong>Language:</strong> <span id='previewLanguage'></span></p>");

            out.println("<p><strong>Duration:</strong> <span id='previewDuration'></span></p>");

            out.println("<p class='movie-description' id='previewDescription'></p>");

            out.println("</div>");
            out.println("</div>");

            out.println("<label>Select Theatre</label>");

            out.println("<select name='theatreId' required>");
            out.println("<option value=''>Choose a theatre</option>");

            if (theatres != null) {
                for (Theatre theatre : theatres) {

                    out.println("<option value='" + theatre.getTheatreId()
                            + "'>" + escape(theatre.getTheatreName())
                            + " - " + escape(theatre.getLocation())
                            + "</option>");
                }
            }

            out.println("</select>");

            if (theatres == null || theatres.isEmpty()) {
                out.println("<p class='empty'>No theatres found. Add a theatre first.</p>");
            }

            out.println("<label>Show Date</label>");

            out.println("<input type='date' name='showDate' min='"
                    + LocalDate.now() + "' required>");

            out.println("<label>Show Time</label>");

            out.println("<input type='time' name='showTime' required>");

            out.println("<label>Ticket Price (₹)</label>");

            out.println("<input type='number' name='ticketPrice' "
                    + "min='1' step='0.01' placeholder='Enter ticket price' required>");

            out.println("<button class='submit' type='submit'>Add Show</button>");

            out.println("</form>");

            out.println("<a class='back' href='" + context
                    + "/ShowListServlet'>← Back to Show Management</a>");

            out.println("</div>");
            out.println("</main>");

            out.println("<script>");

            out.println("const movieSelect = document.getElementById('movieId');");

            out.println("const moviePreview = document.getElementById('moviePreview');");

            out.println("const previewPoster = document.getElementById('previewPoster');");

            out.println("movieSelect.addEventListener('change', function() {");

            out.println("const selected = this.options[this.selectedIndex];");

            out.println("if (!selected.value) {");

            out.println("moviePreview.style.display = 'none';");

            out.println("return;");

            out.println("}");

            out.println("document.getElementById('previewTitle').textContent = selected.dataset.title || '';");

            out.println("document.getElementById('previewGenre').textContent = selected.dataset.genre || 'Not available';");

            out.println("document.getElementById('previewLanguage').textContent = selected.dataset.language || 'Not available';");

            out.println("document.getElementById('previewDuration').textContent = selected.dataset.duration ? selected.dataset.duration + ' minutes' : 'Not available';");

            out.println("document.getElementById('previewDescription').textContent = selected.dataset.description || 'Description not available';");

            out.println("if (selected.dataset.poster) {");

            out.println("previewPoster.src = selected.dataset.poster;");

            out.println("previewPoster.style.display = 'block';");

            out.println("} else {");

            out.println("previewPoster.removeAttribute('src');");

            out.println("previewPoster.style.display = 'none';");

            out.println("}");

            out.println("moviePreview.style.display = 'flex';");

            out.println("});");

            out.println("</script>");

            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {

            log("Unable to load Add Show form", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load movies and theatres."
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

            String movieValue = request.getParameter("movieId");
            String theatreValue = request.getParameter("theatreId");
            String showDate = request.getParameter("showDate");
            String showTime = request.getParameter("showTime");
            String priceValue = request.getParameter("ticketPrice");

            if (movieValue == null || movieValue.trim().isEmpty()
                    || theatreValue == null || theatreValue.trim().isEmpty()
                    || showDate == null || showDate.trim().isEmpty()
                    || showTime == null || showTime.trim().isEmpty()
                    || priceValue == null || priceValue.trim().isEmpty()) {

                response.sendRedirect(context + "/ShowServlet?error=empty");
                return;
            }

            int movieId = Integer.parseInt(movieValue);
            int theatreId = Integer.parseInt(theatreValue);

            LocalDate date = LocalDate.parse(showDate);
            LocalTime time = LocalTime.parse(showTime);

            double ticketPrice = Double.parseDouble(priceValue);

            if (movieId <= 0 || theatreId <= 0
                    || date.isBefore(LocalDate.now())
                    || ticketPrice <= 0
                    || !Double.isFinite(ticketPrice)) {

                response.sendRedirect(context + "/ShowServlet?error=invalid");
                return;
            }

            Show show = new Show();

            show.setMovieId(movieId);
            show.setTheatreId(theatreId);
            show.setShowDate(showDate);
            show.setShowTime(time.toString());
            show.setTicketPrice(ticketPrice);

            boolean result = showDAO.addShow(show);

            if (result) {
                response.sendRedirect(context
                        + "/ShowListServlet?message=added");
            } else {
                response.sendRedirect(context + "/ShowServlet?error=failed");
            }

        } catch (NumberFormatException | java.time.DateTimeException e) {

            response.sendRedirect(context + "/ShowServlet?error=invalid");

        } catch (IOException e) {

            log("Error while adding show", e);

            response.sendRedirect(context + "/ShowServlet?error=failed");
        }
    }
}
