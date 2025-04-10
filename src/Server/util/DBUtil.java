package Server.util;

import javax.sql.DataSource;
import java.sql.*;



public class DBUtil {

    private static DataSource dataSource;
    private static Connection con;

    static {
        // TODO: Connection setup
    }

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

    /**
     * Retrieves an existing account with the specified credentials.
     * @param userName
     * @param password
     * @return
     */
    public static boolean checkAccount(String userName, String password) {
        try {
            String query = "SELECT * FROM player WHERE username = ? AND password = ?";
            PreparedStatement prepstmt = con.prepareStatement(query);
            prepstmt.setString(1, userName);
            prepstmt.setString(2, password);
            ResultSet res = prepstmt.executeQuery();
            if (res.next())
                return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }



    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
