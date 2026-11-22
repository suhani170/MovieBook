package test;

import db.DBConnection;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try {

            Connection con = DBConnection.getConnection();

            if (con != null) {
                System.out.println("JDBC Connection Successful!");
                con.close();
            } else {
                System.out.println("JDBC Connection Failed!");
            }

        } catch (Exception e) {

            System.out.println("JDBC Connection Failed!");
            e.printStackTrace();
        }
    }
}