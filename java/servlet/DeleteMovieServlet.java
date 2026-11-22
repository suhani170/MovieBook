
package servlet;

import dao.MovieDAO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteMovieServlet")
public class DeleteMovieServlet extends HttpServlet {

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

            int movieId = Integer.parseInt(
                    request.getParameter("id"));

            if (movieId <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid movie ID."
                );
                return;
            }

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Delete Movie - MovieBook</title>");

            out.println("<style>");
            out.println("*{margin:0;padding:0;box-sizing:border-box;}");
            out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:#fff;}");
            out.println("nav{background:#160e27;padding:18px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");
            out.println(".logo{font-size:23px;font-weight:bold;color:#b78aff;text-decoration:none;}");
            out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");
            out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");
            out.println(".nav-links a:hover{color:#b78aff;}");
            out.println(".container{width:90%;max-width:550px;margin:80px auto;background:#160e27;padding:35px;border:1px solid #49345f;border-radius:14px;text-align:center;}");
            out.println("h1{color:#ff7777;margin-bottom:18px;font-size:27px;}");
            out.println("p{color:#ccc;line-height:1.7;margin-bottom:15px;}");
            out.println(".warning{color:#ff9999;font-size:14px;}");
            out.println(".actions{display:flex;justify-content:center;gap:15px;flex-wrap:wrap;margin-top:25px;}");
            out.println("button,.cancel{padding:12px 22px;border:0;border-radius:7px;font-size:14px;font-weight:bold;text-decoration:none;cursor:pointer;}");
            out.println(".delete{background:#dc3545;color:white;}");
            out.println(".delete:hover{background:#b52a37;}");
            out.println(".cancel{background:#49317a;color:white;}");
            out.println(".cancel:hover{background:#6545a0;}");
            out.println("@media(max-width:600px){.container{margin:40px auto;padding:25px;}nav{padding:15px;}}");
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

            out.println("<h1>Delete Movie?</h1>");
            out.println("<p>Are you sure you want to delete movie ID <strong>"
                    + movieId + "</strong>?</p>");
            out.println("<p class='warning'>This action cannot be undone.</p>");

            out.println("<div class='actions'>");

            out.println("<form action='" + context
                    + "/DeleteMovieServlet' method='post'>");

            out.println("<input type='hidden' name='id' value='"
                    + movieId + "'>");

            out.println("<button class='delete' type='submit'>Yes, Delete Movie</button>");

            out.println("</form>");

            out.println("<a class='cancel' href='" + context
                    + "/MovieListServlet'>Cancel</a>");

            out.println("</div>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid movie ID."
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        try {

            int movieId = Integer.parseInt(
                    request.getParameter("id"));

            if (movieId <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid movie ID."
                );
                return;
            }

            boolean deleted = movieDAO.deleteMovie(movieId);

            if (deleted) {

                response.sendRedirect(
                        context + "/MovieListServlet?deleted=1"
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_CONFLICT,
                        "Movie could not be deleted. It may not exist or may be linked to existing shows."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid movie ID."
            );

        } catch (IOException e) {

            log("Error while deleting movie", e);

            response.sendError(
                    HttpServletResponse.SC_CONFLICT,
                    "Movie could not be deleted. Check whether it is linked to existing shows or bookings."
            );
        }
    }
}
