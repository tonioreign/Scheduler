package sample.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.db.AppointmentDB;
import sample.db.UserDB;
import sample.misc.AccessMethod;
import sample.models.Appointments;
import sample.models.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class that provides control logic for the login form of the application.
 *
 * @author Antonio Jenkins
 */

public class LoginController implements Initializable {
    /**The login main plane*/
    @FXML
    private AnchorPane LoginAnchor;
    /**The login user field*/
    @FXML
    private TextField UserField;
    /**The login title*/
    @FXML
    private Label LoginLabel;
    /**The login username label*/
    @FXML
    private Label UserLabel;
    /**The login password field*/
    @FXML
    private PasswordField PassField;
    /**The login password label*/
    @FXML
    private Label PassLabel;
    /**The login button*/
    @FXML
    private Button LoginButton;
    /**The login exit button*/
    @FXML
    private Button ExitButton;
    /**The login time zone label*/
    @FXML
    private Label TimeLabel;
    /**Setting the login time zone label*/
    @FXML
    private Label SetTimeLabel;

    Parent parent;
    Stage stage;
    Scene scene;

    /**Button that exits the login form - closes the program*/
    @FXML
    void onExitButton(ActionEvent event) {
        System.exit(0);
    }

    /**Button that opens " " form after confirmation of the login details*/
    @FXML
    void onLoginButton(ActionEvent event) throws IOException {
        try {
            //definitions for +/- 15 minute appointment check
            ObservableList<Appointments> getAllAppointments = AppointmentDB.getAllAppointments();
            LocalDateTime currentTimeMinus15Min = LocalDateTime.now().minusMinutes(15);
            LocalDateTime currentTimePlus15Min = LocalDateTime.now().plusMinutes(15);
            LocalDateTime startTime;
            int getAppointmentID = 0;
            LocalDateTime displayTime = null;
            boolean appointmentWithin15Min = false;

            //ResourceBundle rb = ResourceBundle.getBundle("lang/login", Locale.getDefault());

            String usernameInput = UserField.getText();
            String passwordInput = PassField.getText();
            int userId = UserDB.validateUser(usernameInput, passwordInput);

            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);

            if (userId > 0) {
                AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");

                //log the successful login
                outputFile.print("user: " + usernameInput + " successfully logged in at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

                //check for upcoming appointments if user is validated
                for (Appointments appointment: getAllAppointments) {
                    startTime = appointment.getApmtStart();
                    if ((startTime.isAfter(currentTimeMinus15Min) || startTime.isEqual(currentTimeMinus15Min)) && (startTime.isBefore(currentTimePlus15Min) || (startTime.isEqual(currentTimePlus15Min)))) {
                        getAppointmentID = appointment.getApmtId();
                        displayTime = startTime;
                        appointmentWithin15Min = true;
                    }
                }
                if (appointmentWithin15Min !=false) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment within 15 minutes: " + getAppointmentID + " and appointment start time of: " + displayTime);
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    System.out.println("There is an appointment within 15 minutes");
                }

                else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No upcoming appointments.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    System.out.println("no upcoming appointments");
                }
            } else if (userId < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                //alert.setTitle(rb.getString("Error"));
                //alert.setContentText(rb.getString("Incorrect"));
                alert.show();

                //log the failed login attempt
                outputFile.print("user: " + usernameInput + " failed login attempt at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

            }
            outputFile.close();
        } catch (IOException | SQLException ioException) {
            ioException.printStackTrace();
        }

    }
    /**Resets username and password field to empty*/
    @FXML
    void onReset(ActionEvent event){
        UserField.setText("");
        PassField.setText("");
    }
    /***/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {

            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);

            ZoneId zone = ZoneId.systemDefault();
            SetTimeLabel.setText(String.valueOf(zone));

            //ResourceBundle rb = ResourceBundle.getBundle("lang/login.properties", Locale.getDefault());
            //UserField.setText(rb.getString("username"));
            //PassField.setText(rb.getString("password"));
            //LoginButton.setText(rb.getString("Login"));
            //ExitButton.setText(rb.getString("Exit"));
            //TimeLabel.setText(rb.getString("Location"));

        } catch(MissingResourceException e) {
            System.out.println("Resource file missing: " + e);
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
