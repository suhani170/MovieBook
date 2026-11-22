
package dao;

import db.DBConnection;
import model.Theatre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TheatreDAO {

    // Map database record to Theatre object
    private Theatre mapTheatre(ResultSet rs) throws SQLException {

        Theatre theatre = new Theatre();

        theatre.setTheatreId(rs.getInt("theatre_id"));
        theatre.setTheatreName(rs.getString("theatre_name"));
        theatre.setLocation(rs.getString("location"));

        return theatre;
    }

    // CREATE - Add Theatre
    public boolean addTheatre(Theatre theatre) {

        if (theatre == null) {
            return false;
        }

        String sql = "INSERT INTO theatres "
                + "(theatre_name, location) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, theatre.getTheatreName());
            ps.setString(2, theatre.getLocation());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding theatre:");
            e.printStackTrace();
            return false;
        }
    }

    // READ - Get All Theatres
    public List<Theatre> getAllTheatres() {

        List<Theatre> theatres = new ArrayList<>();

        String sql = "SELECT * FROM theatres ORDER BY theatre_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                theatres.add(mapTheatre(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching theatres:");
            e.printStackTrace();
        }

        return theatres;
    }

    // READ - Get Theatre By ID
    public Theatre getTheatreById(int theatreId) {

        String sql = "SELECT * FROM theatres WHERE theatre_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, theatreId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapTheatre(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching theatre:");
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE - Update Theatre
    public boolean updateTheatre(Theatre theatre) {

        if (theatre == null) {
            return false;
        }

        String sql = "UPDATE theatres SET "
                + "theatre_name = ?, location = ? "
                + "WHERE theatre_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, theatre.getTheatreName());
            ps.setString(2, theatre.getLocation());
            ps.setInt(3, theatre.getTheatreId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating theatre:");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Delete Theatre
    public boolean deleteTheatre(int theatreId) {

        String sql = "DELETE FROM theatres WHERE theatre_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, theatreId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error deleting theatre. It may have existing shows:"
            );
            e.printStackTrace();
            return false;
        }
    }
}
