package Server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                //Class.forName(DatabaseConfig.DB_DRIVER);
                connection = DriverManager.getConnection(
                        DatabaseConfig.DB_URL,
                        DatabaseConfig.DB_USER,
                        DatabaseConfig.DB_PASSWORD
                );
//            } catch (ClassNotFoundException e) {
//                System.err.println("JDBC Driver Not Found");
//                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
}
