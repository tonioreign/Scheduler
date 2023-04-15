package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.User;
import sample.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This class provides methods for retrieving and managing user data in a database.
 * It allows for retrieving all user data from the database and populating an ObservableList of User objects,
 * as well as validating user credentials for a login form.
 *
 * @author Antonio Jenkins
 */
public class UserDB {
    /**
     * Connection object to the database.
     */
    private static Connection conn = DBConnection.openConnection();

    /**
     * ObservableList to store User objects.
     */
    public static ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * String to store SQL query.
     */
    private static String sqlQuery = null;

    /**
     * PreparedStatement object for executing SQL queries.
     */
    private static PreparedStatement ps = null;

    /**
     * ResultSet object to store the result of SQL queries.
     */
    private static ResultSet rs = null;


    /**
     * Retrieves all user data from the database and populates the usersObservableList.
     *
     * @return usersObservableList - the ObservableList of User objects containing user data
     * @throws SQLException - if there is an error accessing the database
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> usersObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String userPassword = rs.getString("Password");
                User user = new User(userID, userName, userPassword);
                usersObservableList.add(user);
            }
        } catch (SQLException e) {
            // Handle any SQL exception that may occur
            e.printStackTrace();
            throw e;
        }
        return usersObservableList;
    }

    /**
     * Validates a user's credentials for the login form.
     *
     * @param username - the username to be validated
     * @param password - the password to be validated
     * @return userID - the user ID if the credentials are valid, or -1 if not
     */
    public static int validateUser(String username, String password) {
        try {
            sqlQuery = "SELECT * FROM users WHERE user_name = ? AND password = ?";
            ps = DBConnection.getConnection().prepareStatement(sqlQuery);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("User_Name").equals(username) && rs.getString("Password").equals(password)) {
                    return rs.getInt("User_ID");
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exception that may occur
            e.printStackTrace();
        } finally {
            // Close PreparedStatement and ResultSet
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }

}
