package Server.DataAccessObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Server.WhatsTheWord.referenceClasses.Player;
import Server.database.DatabaseConnection;
import Server.exception.DataAccessException;

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

    public static void deletePlayer(String username) {
        // TODO: delete player from db
    }


    public static void findByUserName(String username) {

    }

    public static void setLobbyWaitingTime() {
        // TODO: edit lobby waiting time
    }

    public static void setRoundTime() {
        // TODO: edit lobby waiting time
    }

    public static void addPlayer(String username, String password) {
        Player newPlayer = new Player(0, username, password, 0, 0, 0, false);
        PlayerDAO.create(newPlayer);
    }

    public List<Player> findAllPlayers() {
        List<Player> players = new ArrayList<>();

        String query = "SELECT * FROM player ORDER BY wins DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while(rs.next()) {
                Player player = new Player();
                player.username = rs.getString( "username");
                player.password = rs.getString("password");
                player.wins = rs.getInt("wins");
                player.noOfRoundWins = rs.getInt("noOfRoundWins");
                player.time = rs.getInt("time");
                player.hasPlayed = rs.getBoolean("hasPlayed");
                players.add(player);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete player");
        }
        return players;
    }
}
