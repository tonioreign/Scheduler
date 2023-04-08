package sample.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import sample.models.User;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUser {

    private static Connection conn = DBConnection.openConnection();
    public static ObservableList<User> users = FXCollections.observableArrayList();

    public static void getAllUsers(){
        try{

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
