package Server.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DBUtil {
    private static DataSource dataSource;

    static {
        // TODO: Connection setup
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
