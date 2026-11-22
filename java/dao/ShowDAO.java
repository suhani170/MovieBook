
package dao;

import db.DBConnection;
import model.Show;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO {

    // CREATE - Add Show
    public boolean addShow(Show show) {

        if (show == null) {
            return false;
        }

        String sql = "INSERT INTO shows "
                + "(movie_id, theatre_id, show_date, show_time, ticket_price) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, show.getMovieId());
            ps.setInt(2, show.getTheatreId());
            ps.setString(3, show.getShowDate());
            ps.setString(4, show.getShowTime());
            ps.setDouble(5, show.getTicketPrice());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding show:");
            e.printStackTrace();
            return false;
        }
    }

    // READ - Get All Shows
    public List<Show> getAllShows() {

        List<Show> shows = new ArrayList<>();

        String sql = "SELECT * FROM shows "
                + "ORDER BY show_date DESC, show_time DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                shows.add(mapShow(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching shows:");
            e.printStackTrace();
        }

        return shows;
    }

    // READ - Get Show By ID
    public Show getShowById(int showId) {

        String sql = "SELECT * FROM shows WHERE show_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapShow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching show:");
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE - Update Show
    public boolean updateShow(Show show) {

        if (show == null) {
            return false;
        }

        String sql = "UPDATE shows SET "
                + "movie_id = ?, "
                + "theatre_id = ?, "
                + "show_date = ?, "
                + "show_time = ?, "
                + "ticket_price = ? "
                + "WHERE show_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, show.getMovieId());
            ps.setInt(2, show.getTheatreId());
            ps.setString(3, show.getShowDate());
            ps.setString(4, show.getShowTime());
            ps.setDouble(5, show.getTicketPrice());
            ps.setInt(6, show.getShowId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating show:");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Delete Show
    public boolean deleteShow(int showId) {

        String sql = "DELETE FROM shows WHERE show_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, showId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error deleting show. It may have existing bookings:"
            );
            e.printStackTrace();
            return false;
        }
    }

    // Map database record to Show object
    private Show mapShow(ResultSet rs) throws SQLException {

        Show show = new Show();

        show.setShowId(rs.getInt("show_id"));
        show.setMovieId(rs.getInt("movie_id"));
        show.setTheatreId(rs.getInt("theatre_id"));
        show.setShowDate(rs.getString("show_date"));
        show.setShowTime(rs.getString("show_time"));
        show.setTicketPrice(rs.getDouble("ticket_price"));

        return show;
    }
}
