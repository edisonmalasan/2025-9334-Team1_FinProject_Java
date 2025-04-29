package Server.DataAccessObject;

import Server.WhatsTheWord.referenceClasses.Player;
import Server.database.DatabaseConnection;
import Server.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

// TODO: Implement PlayerDAO methods to interact with the 'players' table using JDBC, including querying by username and mapping results to Player model objects.
public class PlayerDAO {

    public void create(Player player) throws DataAccessException {
        String query = "INSERT INTO player (username, password, wins, noOfWins, time, hasPlayed)" + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, player.username);
            statement.setString(2, player.password);
            statement.setInt(3, player.wins);
            statement.setInt(4, player.noOfRoundWins);
            statement.setInt(5, player.time);
            statement.setBoolean(6, player.hasPlayed);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to create player");
        }
    }

    public Player findByUsername(String username) {
        return null;
    }

    public Player findById(String id) {
        return null;
    }

    public Player save(Player player) {
        return null;
    }

    public void update(Player player) {

    }

    public void delete(String username) {

    }

    public List<Player> findAll() {
        return Collections.emptyList();
    }

}

