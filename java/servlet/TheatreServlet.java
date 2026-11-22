
package servlet;

import dao.TheatreDAO;
import model.Theatre;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/TheatreServlet")
public class TheatreServlet extends HttpServlet {

    private TheatreDAO theatreDAO;

    @Override
    public void init() throws ServletException {
        theatreDAO = new TheatreDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String context = request.getContextPath();

        // Check admin login
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("adminUsername") == null) {

            response.sendRedirect(context + "/admin-login.jsp");
            return;
        }

        try {

            String theatreName = request.getParameter("theatreName");
            String location = request.getParameter("location");

            if (theatreName == null || theatreName.trim().isEmpty()
                    || location == null || location.trim().isEmpty()) {

                response.sendRedirect(
                        context + "/theatre.jsp?error=empty"
                );
                return;
            }

            Theatre theatre = new Theatre();

            theatre.setTheatreName(theatreName.trim());
            theatre.setLocation(location.trim());

            boolean result = theatreDAO.addTheatre(theatre);

            if (result) {

                response.sendRedirect(
                        context + "/TheatreListServlet?message=added"
                );

            } else {

                response.sendRedirect(
                        context + "/theatre.jsp?error=failed"
                );
            }

        } catch (IOException e) {

            log("Error while adding theatre", e);

            response.sendRedirect(
                    context + "/theatre.jsp?error=failed"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath() + "/theatre.jsp"
        );
    }
}
