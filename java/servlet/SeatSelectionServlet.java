
package servlet;

import dao.CustomerBookingDAO;
import dao.ShowInfoDAO;
import model.ShowInfo;
import util.AuthUtil;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Seat selection page: shows the seat grid and which seats are taken.
 * URL: SeatSelection?showId=4
 */
@WebServlet("/SeatSelection")
public class SeatSelectionServlet extends HttpServlet {

    // Seat layout: rows A-F, 10 seats per row
    public static final String ROWS = "ABCDEF";
    public static final int SEATS_PER_ROW = 10;

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

        if (AuthUtil.getUserId(request) == null) {
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

        Set<String> bookedSeats =
                bookingDAO.getBookedSeats(showId);

        request.setAttribute("show", show);
        request.setAttribute("bookedSeats", bookedSeats);
        request.setAttribute("rows", ROWS);
        request.setAttribute("seatsPerRow", SEATS_PER_ROW);

        request.getRequestDispatcher("/seats.jsp")
               .forward(request, response);
    }

    // Used when ConfirmBookingServlet forwards back with an error.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}
