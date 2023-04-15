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
    /**
     * Anchor pane for the login panel in the UI.
     */
    @FXML
    private AnchorPane LoginAnchor;

    /**
     * Text field for the login user input.
     */
    @FXML
    private TextField UserField;

    /**
     * Label for the login title.
     */
    @FXML
    private Label LoginLabel;

    /**
     * Label for the login username.
     */
    @FXML
    private Label UserLabel;

    /**
     * Password field for the login password input.
     */
    @FXML
    private PasswordField PassField;

    /**
     * Label for the login password.
     */
    @FXML
    private Label PassLabel;

    /**
     * Button for initiating the login operation.
     */
    @FXML
    private Button LoginButton;

    /**
     * Button for exiting the login screen.
     */
    @FXML
    private Button ExitButton;

    /**
     * Label for displaying the time zone information in the login UI.
     */
    @FXML
    private Label TimeLabel;

    /**
     * Label for setting the time zone information in the login UI.
     */
    @FXML
    private Label SetTimeLabel;

    /**
     * Parent object for the login UI.
     */
    private Parent parent;

    /**
     * Stage object for the login UI.
     */
    private Stage stage;

    /**
     * Scene object for the login UI.
     */
    private Scene scene;

    /**
     * Event handler for the "Exit" button.
     * Terminates the Java Virtual Machine (JVM) with an exit status of 0,
     * indicating a successful termination, effectively closing the application
     * and exiting the program.
     *
     * @param event The ActionEvent triggered by the "Exit" button click.
     */
    @FXML
    void onExitButton(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Event handler for the login button. This method is called when the login button is clicked.
     *
     * @param event The ActionEvent representing the login button click event.
     * @throws IOException if an error occurs during input/output operations.
     */
    @FXML
    void onLoginButton(ActionEvent event) throws IOException {
        try {
            Locale locale = Locale.getDefault();
            ResourceBundle rb = ResourceBundle.getBundle("lang/login_FR", locale);

            String usernameInput = UserField.getText();
            String passwordInput = PassField.getText();
            int userId = UserDB.validateUser(usernameInput, passwordInput);

            try (FileWriter fileWriter = new FileWriter("login_activity.txt", true);
                 PrintWriter outputFile = new PrintWriter(fileWriter)) {

                if (userId > 0) {
                    AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
                    outputFile.print("user: " + usernameInput + " successfully logged in at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

                    Appointments appointmentWithin15Min = findUpcomingAppointment();
                    if (appointmentWithin15Min != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment within 15 minutes: " + appointmentWithin15Min.getApmtId() + " and appointment start time of: " + appointmentWithin15Min.getApmtStart());
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("There is an appointment within 15 minutes");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No upcoming appointments.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("No upcoming appointments");
                    }
                } else if (userId < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rb.getString("Error"));
                    alert.setContentText(rb.getString("Incorrect"));
                    alert.show();
                    outputFile.print("user: " + usernameInput + " failed login attempt at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
                }
            }
        } catch (IOException | SQLException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Retrieves the first appointment within 15 minutes from the current time.
     *
     * @return An Appointments object representing the first appointment within 15 minutes if found; otherwise, null.
     * @throws SQLException if there is an error while fetching appointments from the database.
     */
    private Appointments findUpcomingAppointment() throws SQLException {
        ObservableList<Appointments> getAllAppointments = AppointmentDB.getAllAppointments();
        LocalDateTime currentTimeMinus15Min = LocalDateTime.now().minusMinutes(15);
        LocalDateTime currentTimePlus15Min = LocalDateTime.now().plusMinutes(15);

        return getAllAppointments.stream()
                .filter(appointment -> {
                    LocalDateTime startTime = appointment.getApmtStart();
                    return (startTime.isAfter(currentTimeMinus15Min) || startTime.isEqual(currentTimeMinus15Min)) && (startTime.isBefore(currentTimePlus15Min) || startTime.isEqual(currentTimePlus15Min));
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * Event handler for the "Reset" button.
     * Clears the text fields for username and password inputs.
     *
     * @param event The ActionEvent triggered by the "Reset" button click.
     */
    @FXML
    void onReset(ActionEvent event){
        UserField.setText("");
        PassField.setText("");
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is called automatically by JavaFX when the FXML file is loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resource bundle used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Locale locale = Locale.getDefault();
            ResourceBundle rb = ResourceBundle.getBundle("lang/login_FR", locale);

            ZoneId zone = ZoneId.systemDefault();
            SetTimeLabel.setText(zone.toString());

            UserField.setPromptText(rb.getString("username"));
            PassField.setPromptText(rb.getString("password"));
            LoginButton.setText(rb.getString("Login"));
            ExitButton.setText(rb.getString("Exit"));
            TimeLabel.setText(rb.getString("Location"));

        } catch (MissingResourceException e) {
            System.out.println("Resource file missing: " + e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
