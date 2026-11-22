package servlet;

import dao.CustomerBookingDAO;
import model.BookingInfo;
import util.AuthUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Booking history of the logged-in customer.
 */
@WebServlet("/MyBookings")
public class MyBookingsServlet extends HttpServlet {

    private CustomerBookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new CustomerBookingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = AuthUtil.getUserId(request);

        if (userId == null) {
            AuthUtil.redirectToLogin(request, response, "MyBookings");
            return;
        }

        List<BookingInfo> bookings = bookingDAO.getBookingsByUser(userId);

        request.setAttribute("bookings", bookings);

        request.getRequestDispatcher("/my-bookings.jsp")
               .forward(request, response);
    }
}
