
package servlet;

import dao.BookingDAO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DeleteBookingServlet")
public class DeleteBookingServlet extends HttpServlet {

    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
    }

    // GET - Display delete confirmation
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

        int bookingId;

        try {
            bookingId = Integer.parseInt(request.getParameter("id"));

            if (bookingId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            showMessage(response, context,
                    "Invalid Booking ID",
                    "Please select a valid booking.");
            return;
        }

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Delete Booking - MovieBook</title>");

        out.println("<style>");
        out.println("* { box-sizing: border-box; }");
        out.println("body { margin: 0; min-height: 100vh; display: flex; align-items: center; justify-content: center; background: #090810; color: white; font-family: Arial, sans-serif; }");
        out.println(".card { width: 90%; max-width: 480px; padding: 35px; text-align: center; background: #171421; border: 1px solid #30283d; border-radius: 16px; }");
        out.println("h1 { color: #ff858b; font-size: 26px; }");
        out.println("p { color: #c4bbd2; line-height: 1.7; }");
        out.println(".actions { display: flex; justify-content: center; gap: 12px; flex-wrap: wrap; margin-top: 25px; }");
        out.println("button, .back { padding: 12px 20px; border: 0; border-radius: 8px; font-weight: bold; text-decoration: none; cursor: pointer; font-size: 14px; }");
        out.println(".delete { background: #c93648; color: white; }");
        out.println(".delete:hover { background: #e04457; }");
        out.println(".back { background: #30283d; color: white; }");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        out.println("<div class='card'>");
        out.println("<h1>Delete Booking?</h1>");
        out.println("<p>Are you sure you want to permanently delete booking #"
                + bookingId + "?</p>");
        out.println("<p>This action cannot be undone.</p>");

        out.println("<div class='actions'>");

        out.println("<form action='" + context
                + "/DeleteBookingServlet' method='post'>");
        out.println("<input type='hidden' name='id' value='"
                + bookingId + "'>");
        out.println("<button class='delete' type='submit'>Yes, Delete</button>");
        out.println("</form>");

        out.println("<a class='back' href='" + context
                + "/BookingListServlet'>Cancel</a>");

        out.println("</div>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }

    // POST - Delete booking
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getContextPath();

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        try {
            int bookingId = Integer.parseInt(request.getParameter("id"));

            if (bookingId <= 0) {
                throw new NumberFormatException();
            }

            boolean result = bookingDAO.deleteBooking(bookingId);

            if (result) {
                response.sendRedirect(
                        context + "/BookingListServlet?deleted=1");
            } else {
                showMessage(response, context,
                        "Booking Not Found",
                        "The booking could not be found or deleted.");
            }

        } catch (NumberFormatException e) {
            showMessage(response, context,
                    "Invalid Booking ID",
                    "Please provide a valid booking ID.");
        }
    }

    private boolean isAdminLoggedIn(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("adminUsername") != null;
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
        out.println("<title>" + title + " - MovieBook</title>");
        out.println("<style>");
        out.println("body { margin:0; min-height:100vh; display:flex; align-items:center; justify-content:center; background:#090810; color:white; font-family:Arial,sans-serif; }");
        out.println(".card { background:#171421; border:1px solid #30283d; border-radius:14px; padding:35px; text-align:center; width:90%; max-width:450px; }");
        out.println("h2 { color:#b98aff; }");
        out.println("p { color:#c4bbd2; line-height:1.6; }");
        out.println("a { display:inline-block; margin-top:18px; padding:12px 20px; background:#8650e8; color:white; text-decoration:none; border-radius:8px; }");
        out.println("</style></head><body>");
        out.println("<div class='card'>");
        out.println("<h2>" + title + "</h2>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='" + context + "/BookingListServlet'>Back to Bookings</a>");
        out.println("</div></body></html>");
    }
}
