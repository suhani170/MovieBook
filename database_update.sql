-- Run this ONCE in phpMyAdmin (database: movie_booking) -> SQL tab
USE movie_booking;

-- 1) Stops the same seat being booked twice for the same show.
--    (If it fails with "Duplicate entry", delete the duplicate rows in
--     the bookings table first, then run this again.)
ALTER TABLE bookings
  ADD UNIQUE KEY uq_show_seat (show_id, seat_number);
