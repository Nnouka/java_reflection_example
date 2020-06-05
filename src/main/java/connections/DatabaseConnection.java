package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static String url;
    private static DatabaseConnection connection;

    private DatabaseConnection() {
        this("jdbc:mysql://localhost:3306/reflective_jdbc?useSSL=false&serverTimezone=UTC");
    }

    private DatabaseConnection(final String url) {
        if (DatabaseConnection.url == null) DatabaseConnection.url = url;
        else throw new RuntimeException("Construction is not allowed here, please use the getInstance() method instead!");
    }

    public static DatabaseConnection getInstance() {
        return connection == null ? new DatabaseConnection() : connection;
    }
    public static DatabaseConnection getInstance(String url) {
        return connection == null ? new DatabaseConnection(url) : connection;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public Statement getStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void executeUpdate(String sqlStatement) {
        try {
            System.out.println("Executing SQL Statement");
            System.out.println(sqlStatement);

            getStatement().executeUpdate(sqlStatement);

            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
