
package dao;

import db.DBConnection;
import model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // CREATE - Add Booking
    public boolean addBooking(Booking booking) throws SQLException {

        String sql = "INSERT INTO bookings "
                   + "(user_id, show_id, seat_number, total_amount) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getShowId());
            ps.setString(3, booking.getSeatNumber());
            ps.setDouble(4, booking.getTotalAmount());

            return ps.executeUpdate() > 0;
        }
    }

    // READ - Get All Bookings
    public List<Booking> getAllBookings() {

        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM bookings "
                   + "ORDER BY booking_date DESC, booking_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching bookings: "
                    + e.getMessage());
        }

        return bookings;
    }

    // UPDATE - Update Booking
    public boolean updateBooking(Booking booking) {

        String sql = "UPDATE bookings SET "
                   + "user_id = ?, show_id = ?, seat_number = ?, "
                   + "total_amount = ? WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getShowId());
            ps.setString(3, booking.getSeatNumber());
            ps.setDouble(4, booking.getTotalAmount());
            ps.setInt(5, booking.getBookingId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error while updating booking: "
                    + e.getMessage());
        }

        return false;
    }

    // DELETE - Delete Booking
    public boolean deleteBooking(int bookingId) {

        String sql = "DELETE FROM bookings WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error while deleting booking: "
                    + e.getMessage());
        }

        return false;
    }

    // READ - Get Booking By ID
    public Booking getBookingById(int bookingId) {

        String sql = "SELECT * FROM bookings WHERE booking_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapBooking(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching booking: "
                    + e.getMessage());
        }

        return null;
    }

    // Convert database row into Booking object
    private Booking mapBooking(ResultSet rs) throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setShowId(rs.getInt("show_id"));
        booking.setSeatNumber(rs.getString("seat_number"));
        booking.setTotalAmount(rs.getDouble("total_amount"));
        booking.setBookingDate(rs.getString("booking_date"));

        return booking;
    }
}
