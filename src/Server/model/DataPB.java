package Server.model;

import java.sql.*;
import java.sql.DriverManager;

public class DataPB {
    /**
     * The connection to the driver.
     */
    private static Connection con;

    /**
     * Default object constructor.
     */
    public DataPB() {}
    /**
     * Sets the connection to the driver.
     */
    public static void setCon() {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String user = "root";
            String password = "";
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
