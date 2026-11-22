package model;

/**
 * A show together with its movie and theatre details
 * (used on the customer pages, so we don't show raw IDs).
 */
public class ShowInfo {

    private int showId;
    private int movieId;
    private String movieTitle;
    private String theatreName;
    private String location;
    private String showDate;
    private String showTime;
    private double ticketPrice;

    public int getShowId() { return showId; }
    public void setShowId(int showId) { this.showId = showId; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getTheatreName() { return theatreName; }
    public void setTheatreName(String theatreName) { this.theatreName = theatreName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getShowDate() { return showDate; }
    public void setShowDate(String showDate) { this.showDate = showDate; }

    public String getShowTime() { return showTime; }
    public void setShowTime(String showTime) { this.showTime = showTime; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
}
