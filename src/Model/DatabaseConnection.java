package Model;

import java.sql.*;

public class DatabaseConnection {

    private Connection connection;

    public DatabaseConnection(final Driver driver, final String URL, final String userName, String password) {

        try {
            // register driver and create connection to database
            DriverManager.registerDriver(driver);
            this.connection = DriverManager.getConnection(URL, userName, password);
        } catch (SQLException e) {
            System.err.println("Error: Can't connect to database.");
        }

    }

    public Connection getConnection() {
        return connection;
    }

}
