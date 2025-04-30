package Server.DataAccessObject;

import Server.WhatsTheWord.referenceClasses.Player;
import Server.database.DatabaseConnection;
import Server.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

// TODO: Implement PlayerDAO methods to interact with the 'players' table using JDBC, including querying by username and mapping results to Player model objects.
public class PlayerDAO {

    public static void create(Player player) throws DataAccessException {
        String query = "INSERT INTO player (username, password, wins, hasPlayed)" + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, player.username);
            statement.setString(2, player.password);
            statement.setInt(3, player.wins);
            statement.setBoolean(4, player.hasPlayed);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create player");
        }
    }

    public static Player findByUsername(String username) {
        Player player = new Player();
        String query = "SELECT * FROM player WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement prepstmt = connection.prepareStatement(query)) {

            prepstmt.setString(1, username);
            ResultSet rs = prepstmt.executeQuery();
            while(rs.next()){
                player.playerId = rs.getInt(1);
                player.username = rs.getString(2);
                player.password = rs.getString(3);
                player.wins = rs.getInt(4);
                player.hasPlayed = rs.getBoolean(5);
            }
            rs.close();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch player");
        }
        return player;
    }

    public Player findById(String id) {
        Player player = new Player();
        String query = "SELECT * FROM player WHERE pId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement prepstmt = connection.prepareStatement(query)) {

            prepstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = prepstmt.executeQuery();
            while(rs.next()){
                player.playerId = rs.getInt(1);
                player.username = rs.getString(2);
                player.password = rs.getString(3);
                player.wins = rs.getInt(4);
                player.hasPlayed = rs.getBoolean(5);
            }
            rs.close();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch player");
        }
        return player;
    }

    public Player save(Player player) {
        return null;
    }

    public void update(Player player, String username) {
        String query = "UPDATE contact SET username=?, password=?, wins=?, hasPlayed=? WHERE username=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, player.username);
            statement.setString(2, player.password);
            statement.setInt(3, player.wins);
            statement.setBoolean(4, player.hasPlayed);
            statement.setString(5, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update player");
        }
    }

    public void delete(String username) {
        String query = "DELETE FROM player WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete player");
        }
    }

    public List<Player> findAll() {
        String query = "SELECT * FROM player ORDER BY wins DESC";

        return Collections.emptyList();
    }

}

