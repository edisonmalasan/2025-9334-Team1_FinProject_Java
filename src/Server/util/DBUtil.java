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

    /**
     * Returns true if updating of a password is successful
     * @param username
     * @param oldPass
     * @param newPass
     * @return
     */
    public static boolean editPassword(String username, String oldPass, String newPass) {
        try {
            String dbOldPass = "";
            String query1 = "SELECT password FROM player WHERE username = ?";
            PreparedStatement ps1 = con.prepareStatement(query1);
            ps1.setString(1, username);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                dbOldPass = rs.getString(1);
            }
            if (dbOldPass.equals(oldPass)) {
                String query2 = "UPDATE player SET password = ? WHERE username = ?";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ps2.setString(1, newPass);
                ps2.setString(2, username);
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
            return false;
        }
        return false;
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
