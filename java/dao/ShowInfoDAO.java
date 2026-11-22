
package dao;

import db.DBConnection;
import model.ShowInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads shows together with movie and theatre details.
 */
public class ShowInfoDAO {

    private static final String BASE_SQL =
            "SELECT s.show_id, s.movie_id, s.show_date, s.show_time, "
          + "s.ticket_price, m.title, t.theatre_name, t.location "
          + "FROM shows s "
          + "JOIN movies m ON s.movie_id = m.movie_id "
          + "JOIN theatres t ON s.theatre_id = t.theatre_id ";

    // Upcoming shows of one movie
    public List<ShowInfo> getUpcomingShowsByMovie(int movieId) {

        List<ShowInfo> list = new ArrayList<>();

        String sql = BASE_SQL
                + "WHERE s.movie_id = ? "
                + "AND TIMESTAMP(s.show_date, s.show_time) > NOW() "
                + "ORDER BY s.show_date, s.show_time";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching upcoming shows:");
            e.printStackTrace();
        }

        return list;
    }

    // Fetch one upcoming show
    public ShowInfo getShowInfo(int showId) {

        String sql = BASE_SQL
                + "WHERE s.show_id = ? "
                + "AND TIMESTAMP(s.show_date, s.show_time) > NOW()";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching show details:");
            e.printStackTrace();
        }

        return null;
    }

    private ShowInfo map(ResultSet rs) throws SQLException {

        ShowInfo info = new ShowInfo();

        info.setShowId(rs.getInt("show_id"));
        info.setMovieId(rs.getInt("movie_id"));
        info.setMovieTitle(rs.getString("title"));

        String theatreName = rs.getString("theatre_name");

        info.setTheatreName(
                theatreName == null ? "" : theatreName.trim()
        );

        info.setLocation(rs.getString("location"));
        info.setShowDate(rs.getString("show_date"));
        info.setShowTime(rs.getString("show_time"));
        info.setTicketPrice(rs.getDouble("ticket_price"));

        return info;
    }
}
