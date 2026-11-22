
package dao;

import db.DBConnection;
import model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    // Map database record to Movie object
    private Movie mapMovie(ResultSet rs) throws SQLException {

        Movie movie = new Movie();

        movie.setMovieId(rs.getInt("movie_id"));
        movie.setTitle(rs.getString("title"));
        movie.setGenre(rs.getString("genre"));
        movie.setLanguage(rs.getString("language"));
        movie.setDuration(rs.getInt("duration"));
        movie.setDescription(rs.getString("description"));
        movie.setPosterUrl(rs.getString("poster_url"));

        return movie;
    }

    // CREATE - Add Movie
    public boolean addMovie(Movie movie) {

        if (movie == null) {
            return false;
        }

        String sql = "INSERT INTO movies "
                + "(title, genre, language, duration, description, poster_url) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setString(3, movie.getLanguage());
            ps.setInt(4, movie.getDuration());
            ps.setString(5, movie.getDescription());
            ps.setString(6, movie.getPosterUrl());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding movie:");
            e.printStackTrace();
            return false;
        }
    }

    // READ - Get All Movies
    public List<Movie> getAllMovies() {

        List<Movie> movies = new ArrayList<>();

        String sql = "SELECT movie_id, title, genre, language, "
                + "duration, description, poster_url "
                + "FROM movies ORDER BY movie_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movies.add(mapMovie(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching movies:");
            e.printStackTrace();
        }

        return movies;
    }

    // READ - Get Movie By ID
    public Movie getMovieById(int movieId) {

        String sql = "SELECT movie_id, title, genre, language, "
                + "duration, description, poster_url "
                + "FROM movies WHERE movie_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMovie(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching movie:");
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE - Update Movie
    public boolean updateMovie(Movie movie) {

        if (movie == null) {
            return false;
        }

        String sql = "UPDATE movies SET "
                + "title = ?, "
                + "genre = ?, "
                + "language = ?, "
                + "duration = ?, "
                + "description = ?, "
                + "poster_url = COALESCE(?, poster_url) "
                + "WHERE movie_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setString(3, movie.getLanguage());
            ps.setInt(4, movie.getDuration());
            ps.setString(5, movie.getDescription());
            ps.setString(6, movie.getPosterUrl());
            ps.setInt(7, movie.getMovieId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating movie:");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Delete Movie
    public boolean deleteMovie(int movieId) {

        String sql = "DELETE FROM movies WHERE movie_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error deleting movie. It may have existing shows:"
            );
            return false;
        }
    }
}
