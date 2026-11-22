
package servlet;

import dao.BookingDAO;
import model.Booking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/BookingListServlet")
public class BookingListServlet extends HttpServlet {

    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String context = request.getContextPath();

        if (session == null
                || session.getAttribute("adminUsername") == null) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            List<Booking> bookings = bookingDAO.getAllBookings();

            String adminName = escape(
                    String.valueOf(session.getAttribute("adminUsername"))
            );

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Booking List - MovieBook</title>");

            out.println("<link rel='preconnect' href='https://fonts.googleapis.com'>");
            out.println("<link rel='preconnect' href='https://fonts.gstatic.com' crossorigin>");
            out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap' rel='stylesheet'>");

            out.println("<style>");
            out.println("* { box-sizing: border-box; }");
            out.println("body { margin: 0; font-family: Inter, Arial, sans-serif; background: #090810; color: #fff; }");

            out.println(".navbar { min-height: 76px; padding: 0 5%; display: flex; align-items: center; justify-content: space-between; gap: 20px; background: #100e19; border-bottom: 1px solid #292334; }");
            out.println(".logo { color: #a875ff; font-size: 28px; font-weight: 800; text-decoration: none; }");
            out.println(".nav-links { display: flex; align-items: center; gap: 24px; flex-wrap: wrap; }");
            out.println(".nav-links a { color: #ddd5eb; text-decoration: none; font-size: 14px; }");
            out.println(".nav-links a:hover { color: #b98aff; }");
            out.println(".admin-name { color: #b98aff; font-size: 14px; }");

            out.println(".container { width: 92%; max-width: 1450px; margin: 45px auto; }");
            out.println(".heading { display: flex; justify-content: space-between; align-items: center; gap: 15px; flex-wrap: wrap; margin-bottom: 28px; }");
            out.println("h1 { margin: 0; font-size: 32px; }");
            out.println(".subtitle { color: #a9a0bb; margin-top: 9px; font-size: 14px; }");

            out.println(".table-card { background: #171421; border: 1px solid #30283d; border-radius: 16px; overflow-x: auto; }");
            out.println("table { width: 100%; border-collapse: collapse; min-width: 850px; }");
            out.println("th { background: #241b32; color: #c7a5ff; padding: 17px 14px; text-align: center; font-size: 13px; }");
            out.println("td { padding: 16px 14px; text-align: center; border-top: 1px solid #30283d; color: #eee8f7; font-size: 13px; }");
            out.println("tr:hover td { background: #20192b; }");

            out.println(".edit { color: #b98aff; text-decoration: none; font-weight: 700; }");
            out.println(".delete { background: none; border: none; color: #ff777f; font-weight: 700; cursor: pointer; font-size: 13px; font-family: inherit; padding: 0; }");
            out.println(".edit:hover, .delete:hover { text-decoration: underline; }");

            out.println(".add { display: inline-block; margin-top: 24px; padding: 13px 22px; background: #8650e8; color: white; border-radius: 9px; text-decoration: none; font-weight: 700; }");
            out.println(".add:hover { background: #9a6aff; }");
            out.println(".empty { padding: 35px; text-align: center; color: #aaa0bd; }");

            out.println("@media(max-width: 700px) { .navbar { padding: 18px 5%; align-items: flex-start; flex-direction: column; } .nav-links { gap: 14px; } .container { margin: 28px auto; } h1 { font-size: 25px; } }");
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            out.println("<nav class='navbar'>");
            out.println("<a class='logo' href='" + context + "/index.jsp'>🎬 MovieBook</a>");
            out.println("<div class='nav-links'>");
            out.println("<a href='" + context + "/admin-dashboard.jsp'>Dashboard</a>");
            out.println("<a href='" + context + "/MovieListServlet'>Movies</a>");
            out.println("<a href='" + context + "/TheatreListServlet'>Theatres</a>");
            out.println("<a href='" + context + "/ShowListServlet'>Shows</a>");
            out.println("<a href='" + context + "/UserListServlet'>Users</a>");
            out.println("<a href='" + context + "/BookingListServlet'>Bookings</a>");
            out.println("<span class='admin-name'>👤 " + adminName + "</span>");
            out.println("<a href='" + context + "/LogoutServlet'>Logout</a>");
            out.println("</div>");
            out.println("</nav>");

            out.println("<main class='container'>");

            out.println("<div class='heading'>");
            out.println("<div>");
            out.println("<h1>🎟️ Booking List</h1>");
            out.println("<p class='subtitle'>View and manage all customer bookings.</p>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div class='table-card'>");

            if (bookings == null || bookings.isEmpty()) {

                out.println("<div class='empty'>No bookings found.</div>");

            } else {

                out.println("<table>");
                out.println("<thead><tr>");
                out.println("<th>Booking ID</th>");
                out.println("<th>User ID</th>");
                out.println("<th>Show ID</th>");
                out.println("<th>Seat Number</th>");
                out.println("<th>Total Amount</th>");
                out.println("<th>Booking Date</th>");
                out.println("<th>Action</th>");
                out.println("</tr></thead>");

                out.println("<tbody>");

                for (Booking booking : bookings) {

                    int bookingId = booking.getBookingId();

                    out.println("<tr>");

                    out.println("<td>#" + bookingId + "</td>");
                    out.println("<td>" + booking.getUserId() + "</td>");
                    out.println("<td>" + booking.getShowId() + "</td>");
                    out.println("<td>" + escape(booking.getSeatNumber()) + "</td>");
                    out.println("<td>₹" + String.format("%.2f", booking.getTotalAmount()) + "</td>");
                    out.println("<td>" + escape(booking.getBookingDate()) + "</td>");

                    out.println("<td>");

                    out.println("<a class='edit' href='" + context
                            + "/EditBookingServlet?id=" + bookingId
                            + "'>✏️ Edit</a>");

                    out.println(" &nbsp; | &nbsp; ");

                    out.println("<form style='display:inline' action='" + context
                            + "/DeleteBookingServlet' method='get'>");

                    out.println("<input type='hidden' name='id' value='"
                            + bookingId + "'>");

                    out.println("<button type='submit' class='delete'>🗑️ Delete</button>");

                    out.println("</form>");

                    out.println("</td>");
                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
            }

            out.println("</div>");

            out.println("<a class='add' href='" + context
                    + "/booking.jsp'>＋ New Booking</a>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            throw new ServletException("Unable to display bookings.", e);
        }
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
