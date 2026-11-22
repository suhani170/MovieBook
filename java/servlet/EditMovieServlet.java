
package servlet;

import dao.MovieDAO;
import model.Movie;
import util.HtmlUtil;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EditMovieServlet")
public class EditMovieServlet extends HttpServlet {

    private MovieDAO movieDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String context = request.getContextPath();
        PrintWriter out = response.getWriter();

        try {

            int movieId = Integer.parseInt(
                    request.getParameter("id"));

            if (movieId <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid movie ID.");
                return;
            }

            Movie movie = movieDAO.getMovieById(movieId);

            if (movie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Movie not found.");
                return;
            }

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Edit Movie - MovieBook</title>");

            out.println("<style>");
            out.println("*{margin:0;padding:0;box-sizing:border-box;}");
            out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:#fff;}");
            out.println("nav{background:#160e27;padding:18px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");
            out.println(".logo{font-size:23px;font-weight:bold;color:#b78aff;text-decoration:none;}");
            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");
            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");
            out.println(".nav-links a:hover{color:#b78aff;}");
            out.println(".container{width:90%;max-width:650px;margin:40px auto;background:#160e27;padding:30px;border:1px solid #302044;border-radius:14px;box-shadow:0 10px 35px rgba(0,0,0,.3);}");
            out.println("h1{text-align:center;margin-bottom:25px;color:#c5a5ff;font-size:28px;}");
            out.println("label{display:block;margin-top:16px;margin-bottom:7px;font-weight:bold;color:#ddd;}");
            out.println("input,textarea{width:100%;padding:12px;background:#0b0715;border:1px solid #49345f;border-radius:7px;color:#fff;font-size:14px;}");
            out.println("input:focus,textarea:focus{outline:none;border-color:#a78bfa;}");
            out.println("textarea{resize:vertical;}");
            out.println(".submit{width:100%;padding:13px;margin-top:25px;background:#8b5cf6;color:white;border:0;border-radius:7px;font-size:16px;font-weight:bold;cursor:pointer;}");
            out.println(".submit:hover{background:#7040d5;}");
            out.println(".back{display:block;text-align:center;margin-top:20px;color:#b78aff;text-decoration:none;}");
            out.println(".back:hover{text-decoration:underline;}");
            out.println(".note{color:#aaa;font-size:12px;margin-top:6px;}");
            out.println("@media(max-width:600px){.container{padding:20px;margin:25px auto;}h1{font-size:23px;}nav{padding:15px;}}");
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            // Admin navigation
            out.println("<nav>");
            out.println("<a class='logo' href='" + context + "/admin-dashboard.jsp'>MovieBook Admin</a>");
            out.println("<div class='nav-links'>");
            out.println("<a href='" + context + "/admin-dashboard.jsp'>Dashboard</a>");
            out.println("<a href='" + context + "/MovieListServlet'>Movies</a>");
            out.println("<a href='" + context + "/TheatreListServlet'>Theatres</a>");
            out.println("<a href='" + context + "/ShowListServlet'>Shows</a>");
            out.println("<a href='" + context + "/UserListServlet'>Users</a>");
            out.println("<a href='" + context + "/BookingListServlet'>Bookings</a>");
            out.println("<a href='" + context + "/LogoutServlet'>Logout</a>");
            out.println("</div>");
            out.println("</nav>");

            out.println("<main class='container'>");
            out.println("<h1>Edit Movie</h1>");

            out.println("<form action='" + context
                    + "/EditMovieServlet' method='post'>");

            out.println("<input type='hidden' name='movieId' value='"
                    + movie.getMovieId() + "'>");

            out.println("<label for='title'>Movie Title</label>");
            out.println("<input type='text' id='title' name='title' maxlength='150' value='"
                    + HtmlUtil.esc(movie.getTitle()) + "' required>");

            out.println("<label for='genre'>Genre</label>");
            out.println("<input type='text' id='genre' name='genre' maxlength='100' value='"
                    + HtmlUtil.esc(movie.getGenre()) + "' required>");

            out.println("<label for='language'>Language</label>");
            out.println("<input type='text' id='language' name='language' maxlength='100' value='"
                    + HtmlUtil.esc(movie.getLanguage()) + "' required>");

            out.println("<label for='duration'>Duration (minutes)</label>");
            out.println("<input type='number' id='duration' name='duration' min='1' max='600' value='"
                    + movie.getDuration() + "' required>");

            out.println("<label for='posterUrl'>Movie Poster URL</label>");
            out.println("<input type='url' id='posterUrl' name='posterUrl' maxlength='1000' value='"
                    + HtmlUtil.esc(movie.getPosterUrl()) + "' placeholder='https://example.com/poster.jpg'>");
            out.println("<p class='note'>Update the poster URL or leave the existing URL unchanged.</p>");

            out.println("<label for='description'>Description</label>");
            out.println("<textarea id='description' name='description' rows='5' maxlength='2000' required>"
                    + HtmlUtil.esc(movie.getDescription())
                    + "</textarea>");

            out.println("<button class='submit' type='submit'>Update Movie</button>");

            out.println("</form>");

            out.println("<a class='back' href='" + context
                    + "/MovieListServlet'>Back to Movie List</a>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid movie ID."
            );

        } catch (Exception e) {

            log("Error while displaying edit movie form", e);

            if (!response.isCommitted()) {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to load movie details."
                );
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String context = request.getContextPath();

        try {

            int movieId = Integer.parseInt(
                    request.getParameter("movieId"));

            String title = request.getParameter("title");
            String genre = request.getParameter("genre");
            String language = request.getParameter("language");
            String durationText = request.getParameter("duration");
            String posterUrl = request.getParameter("posterUrl");
            String description = request.getParameter("description");

            title = title == null ? "" : title.trim();
            genre = genre == null ? "" : genre.trim();
            language = language == null ? "" : language.trim();
            durationText = durationText == null ? "" : durationText.trim();
            posterUrl = posterUrl == null ? "" : posterUrl.trim();
            description = description == null ? "" : description.trim();

            if (movieId <= 0 || title.isEmpty()
                    || genre.isEmpty() || language.isEmpty()
                    || durationText.isEmpty() || description.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Please fill in all required movie details."
                );
                return;
            }

            int duration = Integer.parseInt(durationText);

            if (duration < 1 || duration > 600) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Duration must be between 1 and 600 minutes."
                );
                return;
            }

            if (!posterUrl.isEmpty()
                    && !posterUrl.matches("(?i)^https?://.+")) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Please enter a valid HTTP or HTTPS poster URL."
                );
                return;
            }

            Movie movie = new Movie();

            movie.setMovieId(movieId);
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setLanguage(language);
            movie.setDuration(duration);
            movie.setPosterUrl(
                    posterUrl.isEmpty() ? null : posterUrl
            );
            movie.setDescription(description);

            boolean updated = movieDAO.updateMovie(movie);

            if (updated) {
                response.sendRedirect(context + "/MovieListServlet");
            } else {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Movie update failed. Please try again."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid movie ID or duration."
            );

        } catch (IOException e) {

            log("Error while updating movie", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while updating the movie."
            );
        }
    }
}
