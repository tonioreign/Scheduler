package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.User;
import sample.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDB {
    private static Connection conn = DBConnection.openConnection();
    public static ObservableList<User> users = FXCollections.observableArrayList();
    private static String sqlQuery = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

    public static int addUser(){


        return -1;
    }

    public static int removeUser(){


        return -1;
    }

    /**
     * Method to pull in all user data to getAllUsers observablelist.
     * @throws SQLException
     * @return usersObservableList
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        String sql = "SELECT * from users";
        ps = DBConnection.getConnection().prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String userPassword = rs.getString("Password");
            User user = new User(userID, userName, userPassword);
            users.add(user);
        }
        return users;
    }

    /**
     * This method validates the user for the login form.
     * @param password
     * @param username
     */

    public static int validateUser(String username, String password)
    {
        try
        {
            sqlQuery = "SELECT * FROM users WHERE user_name = '" + username + "' AND password = '" + password +"'";

            ps = DBConnection.getConnection().prepareStatement(sqlQuery);
            rs = ps.executeQuery();
            rs.next();
            if (rs.getString("User_Name").equals(username))
            {
                if (rs.getString("Password").equals(password))
                {
                    return rs.getInt("User_ID");

                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
}
