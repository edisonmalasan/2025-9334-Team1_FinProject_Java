package Server.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Server.database.DatabaseConnection;

public class AdminDAO {
    /**
     * Retrieves an existing account with the specified credentials.
     * @param userName
     * @param password
     * @return
     */
    public static boolean checkPlayerAccount(String userName, String password) {
        String query = "SELECT `pId` AS 'Player ID', `username`, `password` FROM `player` FROM player WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement prepstmt = con.prepareStatement(query)) {
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
     * Edits the info of a particular player
     * Returns true if the update is succesful, otherwise it returns falls
     *
     * @param username
     * @param toEdit
     * @param newInfo
     * @return
     */
    public static boolean editPlayerName(String username, String toEdit, String newInfo) {
        if (!(toEdit.equalsIgnoreCase("username"))) {
            return false;
        }
        String query = "UPDATE player SET " + toEdit + " = ? WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement prepstmt = con.prepareStatement(query)){
            prepstmt.setString(1, newInfo);
            prepstmt.setString(2, username);
            prepstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean editPlayerPassword(String password, String toEdit, String newInfo) {
        if (!(toEdit.equalsIgnoreCase("username"))) {
            return false;
        }
        String query = "UPDATE player SET " + toEdit + " = ? WHERE password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement prepstmt = con.prepareStatement(query)){
            prepstmt.setString(1, newInfo);
            prepstmt.setString(2, password);
            prepstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean getPasswordHash(String userName, String password) {
        return false;
    }
}
