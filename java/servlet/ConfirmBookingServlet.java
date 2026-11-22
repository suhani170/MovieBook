package servlet;

import dao.CustomerBookingDAO;
import dao.ShowInfoDAO;
import model.ShowInfo;
import util.AuthUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Saves selected seats as bookings */
@WebServlet("/ConfirmBooking")
public class ConfirmBookingServlet extends HttpServlet {

    private static final int MAX_SEATS = 6;

    private ShowInfoDAO showInfoDAO;
    private CustomerBookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        showInfoDAO = new ShowInfoDAO();
        bookingDAO = new CustomerBookingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath() + "/index.jsp"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String context = request.getContextPath();

        int showId;

        try {
            showId = Integer.parseInt(
                    request.getParameter("showId")
            );
        } catch (NumberFormatException e) {
            response.sendRedirect(context + "/index.jsp");
            return;
        }

        if (showId <= 0) {
            response.sendRedirect(context + "/index.jsp");
            return;
        }

        Integer userId = AuthUtil.getUserId(request);

        if (userId == null) {
            AuthUtil.redirectToLogin(
                    request,
                    response,
                    "SeatSelection?showId=" + showId
            );
            return;
        }

        ShowInfo show = showInfoDAO.getShowInfo(showId);

        if (show == null) {
            response.sendRedirect(context + "/index.jsp");
            return;
        }

        Set<String> chosen = new LinkedHashSet<>();
        String[] seatParams = request.getParameterValues("seat");

        String error = null;

        if (seatParams == null || seatParams.length == 0) {
            error = "Please select at least one seat.";
        } else {

            for (String seat : seatParams) {

                if (seat == null
                        || !seat.matches("^[A-F](?:[1-9]|10)$")) {

                    error = "Invalid seat selection.";
                    break;
                }

                if (!chosen.add(seat)) {
                    error = "Duplicate seat selection detected.";
                    break;
                }
            }
        }

        if (error == null && chosen.size() > MAX_SEATS) {
            error = "You can book a maximum of "
                    + MAX_SEATS + " seats at a time.";
        }

        if (error == null) {

            List<String> selectedSeats =
                    new ArrayList<>(chosen);

            error = bookingDAO.bookSeats(
                    userId,
                    showId,
                    selectedSeats,
                    show.getTicketPrice()
            );
        }

        if (error == null) {

            response.sendRedirect(
                    context + "/MyBookings?booked=" + chosen.size()
            );

            return;
        }

        request.setAttribute("error", error);

        request.getRequestDispatcher(
                "/SeatSelection?showId=" + showId
        ).forward(request, response);
    }
}
