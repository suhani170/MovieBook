
package servlet;

import dao.MovieDAO;
import model.Movie;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {

    private MovieDAO movieDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("adminUsername") != null;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/admin-login.jsp"
            );
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        String context = request.getContextPath();
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Add Movie - MovieBook</title>");

        out.println("<style>");
        out.println("*{margin:0;padding:0;box-sizing:border-box;}");
        out.println("body{font-family:Arial,sans-serif;background:#0b0715;color:#fff;}");
        out.println("nav{background:#160e27;padding:18px 5%;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:15px;border-bottom:1px solid #302044;}");
        out.println(".logo{font-size:23px;font-weight:bold;color:#b78aff;text-decoration:none;}");
        out.println(".nav-links{display:flex;gap:18px;flex-wrap:wrap;}");
        out.println(".nav-links a{color:#ddd;text-decoration:none;font-size:14px;}");
        out.println(".nav-links a:hover{color:#b78aff;}");
        out.println(".box{width:90%;max-width:750px;margin:40px auto;background:#160e27;padding:30px;border:1px solid #302044;border-radius:14px;box-shadow:0 10px 35px rgba(0,0,0,.3);}");
        out.println("h1{text-align:center;margin-bottom:25px;color:#c5a5ff;font-size:28px;}");
        out.println("h2{font-size:19px;color:#c5a5ff;margin-bottom:15px;}");
        out.println("label{display:block;margin-top:16px;margin-bottom:7px;font-weight:bold;color:#ddd;}");
        out.println("input,textarea{width:100%;padding:12px;background:#0b0715;border:1px solid #49345f;border-radius:7px;color:#fff;font-size:14px;}");
        out.println("input:focus,textarea:focus{outline:none;border-color:#a78bfa;}");
        out.println("textarea{resize:vertical;}");
        out.println("button{padding:12px 18px;background:#8b5cf6;color:white;border:0;border-radius:7px;font-size:14px;font-weight:bold;cursor:pointer;}");
        out.println("button:hover{background:#7040d5;}");
        out.println(".submit-btn{width:100%;padding:14px;margin-top:25px;font-size:16px;}");
        out.println(".back{display:block;text-align:center;margin-top:20px;color:#b78aff;text-decoration:none;}");
        out.println(".note{color:#aaa;font-size:12px;margin-top:7px;line-height:1.5;}");
        out.println(".search-section{background:#100a1e;border:1px solid #49345f;border-radius:10px;padding:20px;margin-bottom:25px;}");
        out.println(".search-row{display:flex;gap:10px;}");
        out.println(".search-row input{flex:1;min-width:0;}");
        out.println(".search-row button{white-space:nowrap;}");
        out.println("#searchStatus{color:#c5a5ff;font-size:13px;margin-top:12px;}");
        out.println(".results{display:grid;grid-template-columns:repeat(auto-fill,minmax(145px,1fr));gap:15px;margin-top:15px;}");
        out.println(".movie-result{background:#201431;border:1px solid #49345f;border-radius:9px;padding:10px;}");
        out.println(".movie-result img{width:100%;height:205px;object-fit:cover;border-radius:6px;background:#0b0715;}");
        out.println(".movie-result h3{font-size:14px;margin:9px 0 5px;}");
        out.println(".movie-result p{font-size:12px;color:#aaa;margin-bottom:9px;}");
        out.println(".movie-result button{width:100%;padding:9px;font-size:12px;}");
        out.println(".selected-poster{display:none;margin-top:18px;padding:12px;background:#201431;border:1px solid #49345f;border-radius:9px;align-items:center;gap:15px;}");
        out.println(".selected-poster img{width:75px;height:110px;object-fit:cover;border-radius:6px;}");
        out.println(".selected-poster p{color:#b8a0d8;font-size:13px;margin-top:5px;}");
        out.println(".attribution{margin-top:25px;text-align:center;color:#888;font-size:11px;line-height:1.6;}");
        out.println("@media(max-width:600px){.box{padding:20px;margin:25px auto;}.search-row{flex-direction:column;}nav{padding:15px;}.results{grid-template-columns:repeat(2,minmax(0,1fr));}}");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        out.println("<nav>");
        out.println("<a class='logo' href='" + context + "/admin-dashboard.jsp'>MovieBook Admin</a>");
        out.println("<div class='nav-links'>");
        out.println("<a href='" + context + "/admin-dashboard.jsp'>Dashboard</a>");
        out.println("<a href='" + context + "/MovieListServlet'>Movies</a>");
        out.println("<a href='" + context + "/TheatreListServlet'>Theatres</a>");
        out.println("<a href='" + context + "/ShowListServlet'>Shows</a>");
        out.println("<a href='" + context + "/UserListServlet'>Users</a>");
        out.println("<a href='" + context + "/BookingListServlet'>Bookings</a>");
        out.println("<a href='" + context + "/LogoutServlet'>Logout</a>");
        out.println("</div>");
        out.println("</nav>");

        out.println("<main class='box'>");
        out.println("<h1>Add New Movie</h1>");

        out.println("<section class='search-section'>");
        out.println("<h2>Find Movie Automatically</h2>");
        out.println("<p class='note'>Search for a movie and select the correct result. Its poster and details will be filled automatically.</p>");

        out.println("<div class='search-row'>");
        out.println("<input type='search' id='movieSearch' placeholder='Enter movie name, e.g. Interstellar' maxlength='150'>");
        out.println("<button type='button' id='searchButton' onclick='searchMovies()'>Search Movies</button>");
        out.println("</div>");

        out.println("<p id='searchStatus' aria-live='polite'></p>");
        out.println("<div id='searchResults' class='results'></div>");

        out.println("<div id='selectedPoster' class='selected-poster'>");
        out.println("<img id='selectedImage' src='' alt='Selected movie poster'>");
        out.println("<div><strong id='selectedTitle'></strong>");
        out.println("<p>Movie selected successfully.</p></div>");
        out.println("</div>");
        out.println("</section>");

        out.println("<form id='movieForm' action='" + context + "/MovieServlet' method='post'>");

        out.println("<label for='title'>Movie Title</label>");
        out.println("<input type='text' id='title' name='title' maxlength='150' required>");

        out.println("<label for='genre'>Genre</label>");
        out.println("<input type='text' id='genre' name='genre' maxlength='100' required>");

        out.println("<label for='language'>Language</label>");
        out.println("<input type='text' id='language' name='language' maxlength='100' required>");

        out.println("<label for='duration'>Duration (minutes)</label>");
        out.println("<input type='number' id='duration' name='duration' min='1' max='600' required>");

        out.println("<input type='hidden' id='posterUrl' name='posterUrl' value=''>");

        out.println("<p class='note'>If TMDB does not have a poster, you can still add the movie manually.</p>");

        out.println("<label for='description'>Description</label>");
        out.println("<textarea id='description' name='description' rows='5' maxlength='2000' required></textarea>");

        out.println("<button class='submit-btn' type='submit'>Add Movie</button>");
        out.println("</form>");

        out.println("<a class='back' href='" + context + "/MovieListServlet'>Back to Movie List</a>");

        out.println("<div class='attribution'>");
        out.println("Movie information and poster images are provided by The Movie Database (TMDB).");
        out.println("<br>This product uses the TMDB API but is not endorsed or certified by TMDB.");
        out.println("</div>");

        out.println("</main>");

        out.println("<script>");

        out.println("const searchEndpoint = '" + context + "/MoviePosterSearchServlet';");

        out.println("async function searchMovies(){");
        out.println("const query=document.getElementById('movieSearch').value.trim();");
        out.println("const status=document.getElementById('searchStatus');");
        out.println("const results=document.getElementById('searchResults');");
        out.println("const button=document.getElementById('searchButton');");

        out.println("results.replaceChildren();");
        out.println("if(query.length<2){status.textContent='Enter at least two characters.';return;}");

        out.println("status.textContent='Searching movies...';");
        out.println("button.disabled=true;");

        out.println("try{");
        out.println("const response=await fetch(searchEndpoint+'?q='+encodeURIComponent(query));");
        out.println("if(!response.ok){throw new Error('Movie search failed. Please try again.');}");

        out.println("const data=await response.json();");
        out.println("const movies=data.results||[];");

        out.println("if(movies.length===0){status.textContent='No matching movies found.';return;}");

        out.println("status.textContent=movies.length+' movie results found. Select the correct movie.';");

        out.println("movies.forEach(movie=>{");
        out.println("if(!movie.poster_path)return;");

        out.println("const card=document.createElement('div');");
        out.println("card.className='movie-result';");

        out.println("const image=document.createElement('img');");
        out.println("image.src='https://image.tmdb.org/t/p/w500'+movie.poster_path;");
        out.println("image.alt='Movie poster';");
        out.println("image.loading='lazy';");

        out.println("const heading=document.createElement('h3');");
        out.println("heading.textContent=movie.title||'Untitled movie';");

        out.println("const year=document.createElement('p');");
        out.println("year.textContent=movie.release_date?movie.release_date.substring(0,4):'Release year unavailable';");

        out.println("const select=document.createElement('button');");
        out.println("select.type='button';");
        out.println("select.textContent='Select Movie';");

        out.println("select.addEventListener('click',async()=>{");

        out.println("const posterUrl='https://image.tmdb.org/t/p/w500'+movie.poster_path;");
        out.println("document.getElementById('posterUrl').value=posterUrl;");
        out.println("document.getElementById('selectedImage').src=posterUrl;");
        out.println("document.getElementById('selectedTitle').textContent=movie.title||'Selected movie';");
        out.println("document.getElementById('selectedPoster').style.display='flex';");

        out.println("if(!document.getElementById('title').value.trim()){");
        out.println("document.getElementById('title').value=movie.title||'';");
        out.println("}");

        out.println("if(!document.getElementById('description').value.trim()&&movie.overview){");
        out.println("document.getElementById('description').value=movie.overview;");
        out.println("}");

        out.println("if(!document.getElementById('language').value.trim()&&movie.original_language){");
        out.println("document.getElementById('language').value=movie.original_language.toUpperCase();");
        out.println("}");

        out.println("status.textContent='Loading genre and duration...';");
        out.println("select.disabled=true;");

        out.println("try{");
        out.println("const detailResponse=await fetch(searchEndpoint+'?id='+encodeURIComponent(movie.id));");
        out.println("if(!detailResponse.ok){throw new Error('Could not load movie details.');}");

        out.println("const details=await detailResponse.json();");

        out.println("if(details.genres&&details.genres.length>0){");
        out.println("document.getElementById('genre').value=details.genres.map(g=>g.name).join(', ');");
        out.println("}");

        out.println("if(details.runtime&&details.runtime>0){");
        out.println("document.getElementById('duration').value=details.runtime;");
        out.println("}");

        out.println("status.textContent='Movie selected. Genre and duration filled automatically.';");

        out.println("}catch(error){");
        out.println("status.textContent=error.message+' You can enter genre and duration manually.';");
        out.println("}finally{");
        out.println("select.disabled=false;");
        out.println("}");

        out.println("});");

        out.println("card.append(image,heading,year,select);");
        out.println("results.appendChild(card);");
        out.println("});");

        out.println("if(results.children.length===0){status.textContent='No posters available for these results.';}");

        out.println("}catch(error){");
        out.println("status.textContent=error.message;");
        out.println("}finally{button.disabled=false;}");
        out.println("}");

        out.println("document.getElementById('movieSearch').addEventListener('keydown',function(event){");
        out.println("if(event.key==='Enter'){event.preventDefault();searchMovies();}");
        out.println("});");

        out.println("</script>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!isAdmin(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/admin-login.jsp"
            );
            return;
        }

        String title = request.getParameter("title");
        String genre = request.getParameter("genre");
        String language = request.getParameter("language");
        String durationText = request.getParameter("duration");
        String posterUrl = request.getParameter("posterUrl");
        String description = request.getParameter("description");

        title = title == null ? "" : title.trim();
        genre = genre == null ? "" : genre.trim();
        language = language == null ? "" : language.trim();
        durationText = durationText == null ? "" : durationText.trim();
        posterUrl = posterUrl == null ? "" : posterUrl.trim();
        description = description == null ? "" : description.trim();

        String context = request.getContextPath();

        try {

            if (title.isEmpty() || genre.isEmpty()
                    || language.isEmpty() || durationText.isEmpty()
                    || description.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Please fill in all required movie details."
                );
                return;
            }

            if (title.length() > 150 || genre.length() > 100
                    || language.length() > 100
                    || description.length() > 2000) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "One or more movie details exceed the allowed length."
                );
                return;
            }

            int duration = Integer.parseInt(durationText);

            if (duration < 1 || duration > 600) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Duration must be between 1 and 600 minutes."
                );
                return;
            }

            if (!posterUrl.isEmpty()
                    && !posterUrl.matches(
                            "(?i)^https://image\\.tmdb\\.org/t/p/"
                                    + "(w[0-9]+|original)/[A-Za-z0-9/_-]+\\.(jpg|jpeg|png)$"
                    )) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Please select a valid TMDB movie poster."
                );
                return;
            }

            Movie movie = new Movie();

            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setLanguage(language);
            movie.setDuration(duration);
            movie.setPosterUrl(posterUrl.isEmpty() ? null : posterUrl);
            movie.setDescription(description);

            boolean result = movieDAO.addMovie(movie);

            if (result) {
                response.sendRedirect(
                        context + "/MovieListServlet?message=added"
                );
            } else {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Movie could not be added. Please check the database."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid duration. Please enter a valid number."
            );

        } catch (IOException e) {

            log("Error while adding movie", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while adding the movie."
            );
        }
    }
}
