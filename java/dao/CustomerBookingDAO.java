
package dao;

import db.DBConnection;
import model.BookingInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* Booking operations used by customers */
public class CustomerBookingDAO {

    private static final int MAX_SEATS = 6;

    // Get seats already booked for a show
    public Set<String> getBookedSeats(int showId) {

        Set<String> seats = new HashSet<>();

        String sql =
                "SELECT seat_number FROM bookings WHERE show_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(rs.getString("seat_number"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching booked seats: "
                    + e.getMessage());
        }

        return seats;
    }

    // Book all selected seats in one transaction
    public String bookSeats(int userId, int showId,
                            List<String> seats, double pricePerSeat) {

        if (seats == null || seats.isEmpty()) {
            return "Please select at least one seat.";
        }

        if (seats.size() > MAX_SEATS) {
            return "You can book a maximum of "
                    + MAX_SEATS + " seats at a time.";
        }

        if (userId <= 0 || showId <= 0
                || pricePerSeat <= 0
                || Double.isNaN(pricePerSeat)
                || Double.isInfinite(pricePerSeat)) {
            return "Invalid booking details.";
        }

        Set<String> uniqueSeats = new HashSet<>();

        for (String seat : seats) {

            if (seat == null
                    || !seat.matches("^[A-F](?:[1-9]|10)$")) {
                return "Invalid seat selection.";
            }

            if (!uniqueSeats.add(seat)) {
                return "Duplicate seat selection.";
            }
        }

        Connection con = null;

        try {

            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            /*
             * Lock the show row first.
             * This prevents two booking transactions for the same
             * show from checking and inserting seats simultaneously.
             */
            try (PreparedStatement lockShow = con.prepareStatement(
                    "SELECT show_id FROM shows "
                    + "WHERE show_id = ? FOR UPDATE")) {

                lockShow.setInt(1, showId);

                try (ResultSet rs = lockShow.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return "This show is no longer available.";
                    }
                }
            }

            // Check which seats are already booked
            Set<String> alreadyBooked = new HashSet<>();

            try (PreparedStatement check = con.prepareStatement(
                    "SELECT seat_number FROM bookings "
                    + "WHERE show_id = ?")) {

                check.setInt(1, showId);

                try (ResultSet rs = check.executeQuery()) {

                    while (rs.next()) {
                        alreadyBooked.add(
                                rs.getString("seat_number")
                        );
                    }
                }
            }

            // Stop if any selected seat is already booked
            for (String seat : uniqueSeats) {

                if (alreadyBooked.contains(seat)) {
                    con.rollback();

                    return "Seat " + seat
                            + " is already booked. Please choose "
                            + "another seat.";
                }
            }

            // Insert all selected seats
            String sql =
                    "INSERT INTO bookings "
                    + "(user_id, show_id, seat_number, total_amount) "
                    + "VALUES (?, ?, ?, ?)";

            try (PreparedStatement insert = con.prepareStatement(sql)) {

                for (String seat : uniqueSeats) {

                    insert.setInt(1, userId);
                    insert.setInt(2, showId);
                    insert.setString(3, seat);
                    insert.setDouble(4, pricePerSeat);

                    insert.addBatch();
                }

                insert.executeBatch();
            }

            con.commit();
            return null;

        } catch (SQLException e) {

            rollbackQuietly(con);

            if (e.getErrorCode() == 1062) {
                return "Some seats were just booked by someone else. "
                        + "Please choose again.";
            }

            System.out.println("Error while booking seats: "
                    + e.getMessage());

            return "Booking failed. Please try again.";

        } finally {

            if (con != null) {

                try {
                    con.setAutoCommit(true);
                    con.close();

                } catch (SQLException e) {
                    System.out.println(
                            "Error closing booking connection: "
                            + e.getMessage()
                    );
                }
            }
        }
    }

    // Get booking history of one customer
    public List<BookingInfo> getBookingsByUser(int userId) {

        List<BookingInfo> list = new ArrayList<>();

        String sql =
                "SELECT b.booking_id, b.seat_number, b.total_amount, "
                + "b.booking_date, s.show_date, s.show_time, "
                + "m.title, t.theatre_name, t.location "
                + "FROM bookings b "
                + "JOIN shows s ON b.show_id = s.show_id "
                + "JOIN movies m ON s.movie_id = m.movie_id "
                + "JOIN theatres t ON s.theatre_id = t.theatre_id "
                + "WHERE b.user_id = ? "
                + "ORDER BY b.booking_date DESC, b.booking_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    BookingInfo info = new BookingInfo();

                    info.setBookingId(rs.getInt("booking_id"));
                    info.setMovieTitle(rs.getString("title"));

                    String theatreName =
                            rs.getString("theatre_name");

                    info.setTheatreName(
                            theatreName == null
                            ? "" : theatreName.trim()
                    );

                    info.setLocation(rs.getString("location"));
                    info.setShowDate(rs.getString("show_date"));
                    info.setShowTime(rs.getString("show_time"));
                    info.setSeatNumber(rs.getString("seat_number"));
                    info.setTotalAmount(rs.getDouble("total_amount"));
                    info.setBookingDate(rs.getString("booking_date"));

                    list.add(info);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error fetching booking history: "
                    + e.getMessage());
        }

        return list;
    }

    // Roll back safely if a booking fails
    private void rollbackQuietly(Connection con) {

        if (con != null) {

            try {
                con.rollback();

            } catch (SQLException ignored) {
                // Rollback could not be completed.
            }
        }
    }
}
