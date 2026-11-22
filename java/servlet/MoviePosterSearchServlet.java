
package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/MoviePosterSearchServlet")
public class MoviePosterSearchServlet extends HttpServlet {

    private String accessToken;

    private static final int MAX_ATTEMPTS = 3;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;

    @Override
    public void init() throws ServletException {

        Properties properties = new Properties();

        try (InputStream input = getServletContext()
                .getResourceAsStream("/WEB-INF/tmdb.properties")) {

            if (input == null) {
                throw new ServletException(
                        "TMDB configuration file not found in WEB-INF."
                );
            }

            properties.load(input);

            accessToken = properties.getProperty("tmdb.access.token");

            if (accessToken == null || accessToken.trim().isEmpty()
                    || accessToken.contains("PASTE_YOUR")) {
                throw new ServletException(
                        "TMDB API Read Access Token is missing."
                );
            }

            accessToken = accessToken.trim();

        } catch (IOException e) {
            throw new ServletException(
                    "Unable to load TMDB configuration.", e
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("adminUsername") == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Admin login required."
            );
            return;
        }

        String movieId = request.getParameter("id");
        String query = request.getParameter("q");

        String apiUrl;

        if (movieId != null && !movieId.trim().isEmpty()) {

            movieId = movieId.trim();

            if (!movieId.matches("[0-9]{1,12}")) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid movie ID."
                );
                return;
            }

            apiUrl = "https://api.themoviedb.org/3/movie/"
                    + movieId + "?language=en-US";

        } else {

            if (query == null || query.trim().length() < 2) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Enter at least two characters."
                );
                return;
            }

            query = query.trim();

            if (query.length() > 150) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Movie search text is too long."
                );
                return;
            }

            String encodedQuery = URLEncoder.encode(
                    query, StandardCharsets.UTF_8.name()
            );

            apiUrl = "https://api.themoviedb.org/3/search/movie"
                    + "?query=" + encodedQuery
                    + "&include_adult=false"
                    + "&language=en-US"
                    + "&page=1";
        }

        byte[] responseData = null;
        int status = -1;
        IOException lastException = null;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {

            HttpURLConnection connection = null;

            try {
                URL url = new URL(apiUrl);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);

                connection.setRequestProperty(
                        "Authorization", "Bearer " + accessToken
                );

                connection.setRequestProperty(
                        "Accept", "application/json"
                );

                status = connection.getResponseCode();

                if (status == HttpURLConnection.HTTP_OK) {

                    try (InputStream input = connection.getInputStream()) {
                        responseData = input.readAllBytes();
                    }

                    break;

                }

                log("TMDB returned HTTP " + status
                        + " on attempt " + attempt);

                // Retry only temporary server or rate-limit errors.
                if (status != 429 && status < 500) {
                    break;
                }

            } catch (IOException e) {

                lastException = e;

                log("TMDB connection failed on attempt "
                        + attempt + " of " + MAX_ATTEMPTS, e);

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

            if (attempt < MAX_ATTEMPTS) {
                try {
                    Thread.sleep(500L * attempt);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                    response.sendError(
                            HttpServletResponse.SC_BAD_GATEWAY,
                            "Movie database request was interrupted."
                    );
                    return;
                }
            }
        }

        if (responseData != null) {

            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().write(responseData);
            return;
        }

        if (lastException != null) {
            log("TMDB request failed after all attempts.", lastException);
        }

        response.sendError(
                HttpServletResponse.SC_BAD_GATEWAY,
                "Movie database is temporarily unavailable. Please try again."
        );
    }
}
