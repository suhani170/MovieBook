package servlet;

import dao.MovieDAO;
import dao.ShowInfoDAO;
import model.Movie;
import model.ShowInfo;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shows the upcoming shows (theatre, date, time, price) of one movie.
 * URL: MovieShows?movieId=1
 */
@WebServlet("/MovieShows")
public class MovieShowsServlet extends HttpServlet {

    private MovieDAO movieDAO;
    private ShowInfoDAO showInfoDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
        showInfoDAO = new ShowInfoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int movieId;

        try {
            movieId = Integer.parseInt(request.getParameter("movieId"));
        } catch (NumberFormatException e) {
            response.sendRedirect("index.jsp");
            return;
        }

        Movie movie = movieDAO.getMovieById(movieId);

        if (movie == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        List<ShowInfo> shows = showInfoDAO.getUpcomingShowsByMovie(movieId);

        request.setAttribute("movie", movie);
        request.setAttribute("shows", shows);

        request.getRequestDispatcher("/shows.jsp")
               .forward(request, response);
    }
}
