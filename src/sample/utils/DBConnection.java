package sample.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Establishes a connection to the database
 */
public class DBConnection {

    // DBConnection
    /**
     * The protocol used for the JDBC connection.
     */
    private static final String protocol = "jdbc";

    /**
     * The vendor name for the JDBC connection.
     */
    private static final String vendorName = ":mysql:";

    /**
     * The IP address for the JDBC connection.
     */
    private static final String ipAddress = "";

    /**
     * The name of the database for the JDBC connection.
     */
    private static final String dbName = "client_schedule";

    /**
     * The location of the database for the JDBC connection.
     */
    private static final String location = "//localhost/";

// Concatenate JDBC URL
    /**
     * The JDBC URL formed by concatenating the protocol, vendor name, location, and database name.
     */
    private static final String jdbcUrl = protocol + vendorName + location + dbName;

// Driver & connection interface reference
    /**
     * The driver class name for the JDBC connection.
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * The connection object used to establish a JDBC connection.
     */
    private static Connection conn = null;

// Username & password
    /**
     * The username for the JDBC connection.
     */
    private static final String userName = "sqlUser";

    /**
     * The password for the JDBC connection.
     */
    private static final String password = "Passw0rd!";

    /**
     * The prepared statement object used for executing SQL queries.
     */
    private static PreparedStatement preparedStatement = null;

    /**
     * Opens a new database connection using the configured JDBC driver, JDBC URL,
     * username, and password.
     *
     * @return A database connection object of type java.sql.Connection.
     */
    public static Connection openConnection() {
        Connection conn = null;
        try {
            // Load the JDBC driver class
            Class.forName(driver);

            // Establish a database connection
            conn = DriverManager.getConnection(jdbcUrl, userName, password);

            System.out.println("Connection successful");
        } catch(SQLException | ClassNotFoundException e){
            // Print error message to console
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Creates a PreparedStatement object for a given SQL statement using the provided database Connection.
     *
     * @param conn The database Connection object to be used.
     * @param sqlStatement The SQL statement for which a PreparedStatement object needs to be created.
     * @throws SQLException If an error occurs while creating the PreparedStatement object.
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        try {
            // Create a PreparedStatement object for the given SQL statement
            preparedStatement = conn.prepareStatement(sqlStatement);
        } catch(SQLException e){
            // Rethrow the exception for the caller to handle
            throw e;
        }
    }

    /**
     * Retrieves the currently set PreparedStatement object.
     *
     * @return The PreparedStatement object that was previously set using the setPreparedStatement() method.
     */
    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    /**
     * Retrieves the currently established database Connection object.
     *
     * @return The database Connection object that was previously established using the openConnection() method.
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * Closes the currently established database Connection object.
     *
     */
    public static void closeConnection(){
        try {
            // Close the Connection object
            conn.close();
            System.out.println("Connection closed");
        } catch(SQLException e){
            // Print error message to console
            System.out.println(e.getMessage());
        }
    }
}
