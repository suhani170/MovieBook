package test;

import dao.MovieDAO;
import model.Movie;
import java.util.List;

public class TestMovieDAO {

    public static void main(String[] args) {

        MovieDAO dao = new MovieDAO();

        // CREATE
        Movie movie = new Movie();

        movie.setTitle("Avatar");
        movie.setGenre("Sci-Fi");
        movie.setLanguage("English");
        movie.setDuration(162);
        movie.setDescription("A science fiction adventure movie.");

        boolean added = dao.addMovie(movie);

        if (added) {
            System.out.println("Movie Added Successfully!");
        } else {
            System.out.println("Movie Addition Failed!");
        }

        // READ
        List<Movie> movies = dao.getAllMovies();

        System.out.println("\n===== MOVIE LIST =====");

        for (Movie m : movies) {

            System.out.println("Movie ID: " + m.getMovieId());
            System.out.println("Title: " + m.getTitle());
            System.out.println("Genre: " + m.getGenre());
            System.out.println("Language: " + m.getLanguage());
            System.out.println("Duration: " + m.getDuration() + " minutes");
            System.out.println("Description: " + m.getDescription());
            System.out.println("----------------------------");
        }

        // UPDATE
        Movie updateMovie = new Movie();

        updateMovie.setMovieId(1);
        updateMovie.setTitle("Interstellar Updated");
        updateMovie.setGenre("Science Fiction");
        updateMovie.setLanguage("English");
        updateMovie.setDuration(169);
        updateMovie.setDescription("Updated movie description.");

        boolean updated = dao.updateMovie(updateMovie);

        if (updated) {
            System.out.println("Update Test Successful!");
        } else {
            System.out.println("Update Test Failed!");
        }

        // DELETE
        boolean deleted = dao.deleteMovie(4);

        if (deleted) {
            System.out.println("Delete Test Successful!");
        } else {
            System.out.println("Delete Test Failed!");
        }
    }
}