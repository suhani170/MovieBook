
package servlet;

import dao.BookingDAO;
import model.Booking;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {

    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }

    // CREATE - Add Booking
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            int userId = Integer.parseInt(
                    request.getParameter("userId"));

            int showId = Integer.parseInt(
                    request.getParameter("showId"));

            String seatNumber = request.getParameter("seatNumber");

            double totalAmount = Double.parseDouble(
                    request.getParameter("totalAmount"));

            if (userId <= 0 || showId <= 0
                    || seatNumber == null
                    || !seatNumber.trim().matches("^[A-F](10|[1-9])$")
                    || !Double.isFinite(totalAmount)
                    || totalAmount <= 0) {

                showMessage(response, context,
                        "Invalid Booking Details",
                        "Please enter valid user, show, seat and amount details.");
                return;
            }

            Booking booking = new Booking();

            booking.setUserId(userId);
            booking.setShowId(showId);
            booking.setSeatNumber(seatNumber.trim());
            booking.setTotalAmount(totalAmount);

            boolean result = bookingDAO.addBooking(booking);

            if (result) {
                response.sendRedirect(
                        context + "/BookingListServlet?created=1");
            } else {
                showMessage(response, context,
                        "Booking Failed",
                        "The booking could not be saved. Please check the details and try again.");
            }

        } catch (NumberFormatException e) {

            showMessage(response, context,
                    "Invalid Input",
                    "User ID, Show ID and Amount must contain valid numbers.");

        } catch (SQLException e) {

            showMessage(response, context,
                    "Database Error",
                    "The booking could not be saved. The selected seat may already be booked.");

        } catch (Exception e) {

            log("Unexpected error while creating booking.", e);

            showMessage(response, context,
                    "Booking Error",
                    "Something went wrong while creating the booking.");
        }
    }

    // GET - Open booking form
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        response.sendRedirect(context + "/booking.jsp");
    }

    private void showMessage(HttpServletResponse response,
                             String context,
                             String title,
                             String message)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>" + escape(title) + " - MovieBook</title>");

        out.println("<style>");
        out.println("* { box-sizing: border-box; }");
        out.println("body { margin: 0; min-height: 100vh; display: flex; align-items: center; justify-content: center; background: #090810; color: white; font-family: Arial, sans-serif; padding: 20px; }");
        out.println(".card { width: 100%; max-width: 480px; padding: 35px; text-align: center; background: #171421; border: 1px solid #30283d; border-radius: 16px; }");
        out.println("h2 { color: #b98aff; }");
        out.println("p { color: #c4bbd2; line-height: 1.7; }");
        out.println(".actions { display: flex; justify-content: center; gap: 12px; flex-wrap: wrap; margin-top: 25px; }");
        out.println("a { padding: 12px 20px; background: #8650e8; color: white; border-radius: 8px; text-decoration: none; font-weight: bold; }");
        out.println("a:hover { background: #9a6aff; }");
        out.println(".secondary { background: #30283d; }");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");
        out.println("<div class='card'>");
        out.println("<h2>" + escape(title) + "</h2>");
        out.println("<p>" + escape(message) + "</p>");
        out.println("<div class='actions'>");
        out.println("<a href='" + context + "/booking.jsp'>Try Again</a>");
        out.println("<a class='secondary' href='" + context + "/BookingListServlet'>View Bookings</a>");
        out.println("<a class='secondary' href='" + context + "/admin-dashboard.jsp'>Dashboard</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
