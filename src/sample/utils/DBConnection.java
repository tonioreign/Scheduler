package sample.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Establishes a connection to the database
 */
public class DBConnection {

    //DBConnection
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "";
    private static final String dbName = "client_schedule";
    private static final String location = "//localhost/";

    //Concatenate JDBC URL
    private static final String jdbcUrl = protocol + vendorName + location + dbName;

    //Driver & connection interface reference
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    //Username & password
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";

    //connection
    public static Connection openConnection(){
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection successful");
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static Connection getConnection(){
        return conn;
    }

    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("Connection closed");
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
