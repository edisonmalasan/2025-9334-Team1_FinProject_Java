package Server.DataAccessObject;

import Server.database.DatabaseConnection;
import Server.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LobbyDAO {
    public static void create(String players, String winner) throws DataAccessException {
        String query = "INSERT INTO lobby (players, winner)" + "VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, players);
            statement.setString(2, winner);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create lobby");
        }
    }
}