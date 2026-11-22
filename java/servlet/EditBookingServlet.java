
package servlet;

import dao.BookingDAO;
import model.Booking;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/EditBookingServlet")
public class EditBookingServlet extends HttpServlet {

    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }

    // GET - Display booking details
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        try {
            int bookingId = Integer.parseInt(
                    request.getParameter("id"));

            Booking booking = bookingDAO.getBookingById(bookingId);

            if (booking == null) {
                showMessage(response, context,
                        "Booking Not Found",
                        "The requested booking does not exist.");
                return;
            }

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Edit Booking - MovieBook</title>");

            out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap' rel='stylesheet'>");

            out.println("<style>");
            out.println("* { box-sizing: border-box; }");
            out.println("body { margin: 0; font-family: Inter, Arial, sans-serif; background: #090810; color: white; }");

            out.println(".navbar { min-height: 76px; padding: 0 5%; display: flex; align-items: center; justify-content: space-between; gap: 20px; background: #100e19; border-bottom: 1px solid #292334; }");
            out.println(".logo { color: #a875ff; font-size: 28px; font-weight: 800; text-decoration: none; }");
            out.println(".nav-links { display: flex; gap: 22px; flex-wrap: wrap; }");
            out.println(".nav-links a { color: #ddd5eb; text-decoration: none; font-size: 14px; }");
            out.println(".nav-links a:hover { color: #b98aff; }");

            out.println(".container { width: 90%; max-width: 550px; margin: 55px auto; padding: 32px; background: #171421; border: 1px solid #30283d; border-radius: 16px; }");
            out.println("h1 { text-align: center; margin-top: 0; font-size: 28px; }");
            out.println(".subtitle { text-align: center; color: #a9a0bb; margin-bottom: 28px; font-size: 14px; }");

            out.println("label { display: block; margin: 17px 0 8px; color: #d9c9f5; font-size: 14px; font-weight: 600; }");
            out.println("input { width: 100%; padding: 13px; border: 1px solid #40364f; border-radius: 8px; background: #100e19; color: white; font-size: 14px; outline: none; }");
            out.println("input:focus { border-color: #9560f0; }");

            out.println(".submit { margin-top: 25px; background: #8650e8; border: none; border-radius: 9px; color: white; font-weight: 700; cursor: pointer; }");
            out.println(".submit:hover { background: #9a6aff; }");

            out.println(".back { display: block; text-align: center; margin-top: 22px; color: #b98aff; text-decoration: none; font-size: 14px; }");
            out.println(".back:hover { text-decoration: underline; }");

            out.println("@media(max-width: 600px) { .navbar { padding: 18px 5%; flex-direction: column; align-items: flex-start; } .container { padding: 23px; margin: 30px auto; } }");
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            out.println("<nav class='navbar'>");
            out.println("<a class='logo' href='" + context + "/index.jsp'>🎬 MovieBook</a>");
            out.println("<div class='nav-links'>");
            out.println("<a href='" + context + "/admin-dashboard.jsp'>Dashboard</a>");
            out.println("<a href='" + context + "/BookingListServlet'>Bookings</a>");
            out.println("<a href='" + context + "/LogoutServlet'>Logout</a>");
            out.println("</div>");
            out.println("</nav>");

            out.println("<div class='container'>");
            out.println("<h1>✏️ Edit Booking</h1>");
            out.println("<p class='subtitle'>Update the booking information below.</p>");

            out.println("<form action='" + context + "/EditBookingServlet' method='post'>");

            out.println("<input type='hidden' name='bookingId' value='"
                    + booking.getBookingId() + "'>");

            out.println("<label>User ID</label>");
            out.println("<input type='number' name='userId' min='1' value='"
                    + booking.getUserId() + "' required>");

            out.println("<label>Show ID</label>");
            out.println("<input type='number' name='showId' min='1' value='"
                    + booking.getShowId() + "' required>");

            out.println("<label>Seat Number</label>");
            out.println("<input type='text' name='seatNumber' maxlength='3' value='"
                    + escape(booking.getSeatNumber()) + "' required>");

            out.println("<label>Total Amount (₹)</label>");
            out.println("<input type='number' name='totalAmount' min='0.01' step='0.01' value='"
                    + booking.getTotalAmount() + "' required>");

            out.println("<input class='submit' type='submit' value='Update Booking'>");

            out.println("</form>");

            out.println("<a class='back' href='" + context
                    + "/BookingListServlet'>← Back to Booking List</a>");

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {
            showMessage(response, context,
                    "Invalid Booking ID",
                    "Please select a valid booking.");
        }
    }

    // POST - Update booking
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            int bookingId = Integer.parseInt(
                    request.getParameter("bookingId"));

            int userId = Integer.parseInt(
                    request.getParameter("userId"));

            int showId = Integer.parseInt(
                    request.getParameter("showId"));

            String seatNumber = request.getParameter("seatNumber");

            double totalAmount = Double.parseDouble(
                    request.getParameter("totalAmount"));

            if (bookingId <= 0 || userId <= 0 || showId <= 0
                    || seatNumber == null
                    || !seatNumber.matches("^[A-F](10|[1-9])$")
                    || totalAmount <= 0
                    || Double.isNaN(totalAmount)
                    || Double.isInfinite(totalAmount)) {

                showMessage(response, context,
                        "Invalid Booking Details",
                        "Please enter valid booking information.");
                return;
            }

            Booking booking = new Booking();

            booking.setBookingId(bookingId);
            booking.setUserId(userId);
            booking.setShowId(showId);
            booking.setSeatNumber(seatNumber);
            booking.setTotalAmount(totalAmount);

            boolean result = bookingDAO.updateBooking(booking);

            if (result) {
                response.sendRedirect(
                        context + "/BookingListServlet?updated=1");
            } else {
                showMessage(response, context,
                        "Update Failed",
                        "The booking could not be updated. Check the entered IDs and seat availability.");
            }

        } catch (NumberFormatException e) {
            showMessage(response, context,
                    "Invalid Input",
                    "Please enter valid numeric values.");
        }
    }

    private boolean isAdminLoggedIn(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("adminUsername") != null;
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

    private void showMessage(HttpServletResponse response,
                             String context,
                             String title,
                             String message)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>" + escape(title) + " - MovieBook</title>");
        out.println("<style>");
        out.println("body { background:#090810; color:white; font-family:Arial,sans-serif; display:flex; justify-content:center; align-items:center; min-height:100vh; margin:0; }");
        out.println(".box { background:#171421; border:1px solid #30283d; border-radius:14px; padding:35px; text-align:center; max-width:450px; width:90%; }");
        out.println("h2 { color:#b98aff; }");
        out.println("p { color:#c4bbd2; line-height:1.6; }");
        out.println("a { display:inline-block; margin-top:18px; padding:12px 20px; background:#8650e8; color:white; text-decoration:none; border-radius:8px; }");
        out.println("</style></head><body>");
        out.println("<div class='box'>");
        out.println("<h2>" + escape(title) + "</h2>");
        out.println("<p>" + escape(message) + "</p>");
        out.println("<a href='" + context + "/BookingListServlet'>Back to Bookings</a>");
        out.println("</div></body></html>");
    }
}
