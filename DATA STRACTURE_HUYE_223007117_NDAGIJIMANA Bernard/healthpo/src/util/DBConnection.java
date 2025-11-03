package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/healthpo?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // change if needed

    public static Connection getConnection() throws SQLException {
        // Loads driver automatically in modern JDBC; left here for clarity
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {}
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
