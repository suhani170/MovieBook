
package servlet;

import dao.MovieDAO;
import model.Movie;
import util.HtmlUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MovieListServlet")
public class MovieListServlet extends HttpServlet {

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

        try {
            List<Movie> movies = movieDAO.getAllMovies();

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Manage Movies - MovieBook</title>");

            out.println("<style>");

            out.println("*{margin:0;padding:0;box-sizing:border-box;}");
            out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:#fff;}");
            out.println("nav{background:#160e27;padding:18px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");
            out.println(".logo{font-size:24px;font-weight:bold;color:#b78aff;text-decoration:none;}");
            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");
            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");
            out.println(".nav-links a:hover{color:#b78aff;}");
            out.println(".container{width:94%;max-width:1400px;margin:40px auto;}");
            out.println(".heading{display:flex;justify-content:space-between;align-items:center;gap:15px;flex-wrap:wrap;margin-bottom:25px;}");
            out.println("h1{font-size:30px;color:#fff;}");
            out.println(".add{background:#8b5cf6;color:white;padding:12px 20px;border-radius:8px;text-decoration:none;font-weight:bold;}");
            out.println(".add:hover{background:#7040d5;}");
            out.println(".table-box{overflow-x:auto;background:#160e27;border:1px solid #302044;border-radius:12px;padding:15px;}");
            out.println("table{width:100%;border-collapse:collapse;min-width:850px;}");
            out.println("th,td{padding:14px;text-align:left;border-bottom:1px solid #302044;}");
            out.println("th{background:#211435;color:#c5a5ff;}");
            out.println("td{color:#ddd;font-size:14px;}");
            out.println("tr:hover td{background:#211435;}");
            out.println(".edit,.delete{display:inline-block;text-decoration:none;padding:8px 12px;border-radius:6px;margin:3px;font-size:13px;}");
            out.println(".edit{background:#49317a;color:#fff;}");
            out.println(".delete{background:#692c45;color:#fff;}");
            out.println(".empty{text-align:center;padding:40px;color:#aaa;}");
            out.println(".message{color:#aaa;margin-top:15px;}");
            out.println("@media(max-width:600px){.container{width:96%;margin:25px auto;}h1{font-size:23px;}nav{padding:15px;}}");

            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            // Navigation
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

            out.println("<div class='heading'>");
            out.println("<h1>Movie Management</h1>");
            out.println("<a class='add' href='" + context + "/MovieServlet'>+ Add New Movie</a>");
            out.println("</div>");

            out.println("<div class='table-box'>");

            if (movies == null || movies.isEmpty()) {

                out.println("<div class='empty'>");
                out.println("<h2>No movies found</h2>");
                out.println("<p class='message'>Add a movie to display it here.</p>");
                out.println("</div>");

            } else {

                out.println("<table>");
                out.println("<thead><tr>");
                out.println("<th>ID</th>");
                out.println("<th>Title</th>");
                out.println("<th>Genre</th>");
                out.println("<th>Language</th>");
                out.println("<th>Duration</th>");
                out.println("<th>Description</th>");
                out.println("<th>Actions</th>");
                out.println("</tr></thead>");

                out.println("<tbody>");

                for (Movie movie : movies) {

                    int id = movie.getMovieId();

                    out.println("<tr>");

                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + HtmlUtil.esc(movie.getTitle()) + "</td>");
                    out.println("<td>" + HtmlUtil.esc(movie.getGenre()) + "</td>");
                    out.println("<td>" + HtmlUtil.esc(movie.getLanguage()) + "</td>");
                    out.println("<td>" + movie.getDuration() + " min</td>");
                    out.println("<td>" + HtmlUtil.esc(movie.getDescription()) + "</td>");

                    out.println("<td>");

                    out.println("<a class='edit' href='" + context
                            + "/EditMovieServlet?id=" + id + "'>Edit</a>");

                    out.println("<a class='delete' href='" + context
                            + "/DeleteMovieServlet?id=" + id
                            + "' onclick=\"return confirm('Are you sure you want to delete this movie?');\">Delete</a>");

                    out.println("</td>");

                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
            }

            out.println("</div>");
            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {

            log("Error while displaying movies", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load movies. Please try again."
            );
        }
    }
}
