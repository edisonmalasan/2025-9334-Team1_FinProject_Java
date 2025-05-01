package Server.database;

import java.util.Properties;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/whatstheword";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    // private construct to prevent instance
    private DatabaseConfig() {
    }
}
