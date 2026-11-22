
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/movie_booking";

    private static final String DEFAULT_USER = "root";

    private static final String DEFAULT_PASSWORD = "";

    public static Connection getConnection() throws SQLException {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || url.trim().isEmpty()) {
            url = DEFAULT_URL;
        }

        if (user == null || user.trim().isEmpty()) {
            user = DEFAULT_USER;
        }

        if (password == null) {
            password = DEFAULT_PASSWORD;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    url,
                    user,
                    password
            );

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "MySQL JDBC Driver not found. Check MySQL Connector/J.",
                    e
            );

        } catch (SQLException e) {

            throw new SQLException(
                    "Unable to connect to movie_booking database. "
                    + "Check MySQL server, database name and credentials.",
                    e
            );
        }
    }
}
