
package servlet;

import dao.MovieDAO;
import model.Movie;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {

    private MovieDAO movieDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Movie> movies = movieDAO.getAllMovies();

        request.setAttribute("movies", movies);

        if (movies != null && !movies.isEmpty()) {
            request.setAttribute("featured", movies.get(0));
        }

        request.getRequestDispatcher("/index.jsp")
               .forward(request, response);
    }
}
