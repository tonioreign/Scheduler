package sample.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.LoginController;
import sample.utils.DBConnection;

import java.util.Locale;
import java.util.ResourceBundle;

/** This is the main class for the application.
 *  It is responsible for loading the login form and connecting to the database.
 *  It also closes the connection to the database when the application is closed.
 *
 *  The way this application is designed is to be mostly efficient, easy to use, and readable.
 *  Readability was taken into consideration when designing the application.
 *  I used multiple techniques to make the application more efficient using data structures that I have learned over the years.
 *  I also used lambda expressions to make the application more efficient.
 *
 *  *FUTURE IDEAS* - I would like speed up the application by sacrificing more memory, but using O(1) look up times using more Maps, Arrays and more.
 *  *FUTURE IDEAS* - Adding a feature to determine if the customer is "ID Verified" and if they are, then they can be added to the database.
 *  *SECURITY MEASURES* - I would like to add a feature to encrypt the password before it is sent to the database.
 *  *SECURITY MEASURES* - Regularly backup the database to a cloud service.
 *
 *  Scheduling Application was optimized with the help of the following resources:
 *
 *
 * @author Antonio Jenkins
 * @version 1.0
 * */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/login-form.fxml"));
        Locale locale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("resources/login", locale);
        primaryStage.setTitle(resources.getString("title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {

        DBConnection.openConnection();

        launch(args);

        DBConnection.closeConnection();
    }
}
