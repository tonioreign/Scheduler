package sample.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sample.db.AppointmentDB;
import sample.db.UserDB;
import sample.misc.AccessMethod;
import sample.models.Appointments;

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
     * Button for initiating the reset operation on username and password field.
     */
    @FXML
    private Button ResetButton;

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
            String usernameInput = UserField.getText();
            String passwordInput = PassField.getText();
            int userId = UserDB.validateUser(usernameInput, passwordInput);

            try (FileWriter fileWriter = new FileWriter("login_activity.txt", true);
                 PrintWriter outputFile = new PrintWriter(fileWriter)) {

                if (userId > 0) {
                    handleSuccessfulLogin(event, usernameInput, outputFile);
                } else if (userId < 0) {
                    handleFailedLogin(usernameInput, outputFile);
                }
            }
        } catch (IOException | SQLException ioException) {
            ioException.printStackTrace();
        }
        onReset(event);
    }

/**
 * Handles a successful login attempt.
 *
 * @param event        The ActionEvent representing the login button click event.
 * @param usernameInput The input username.
 * @param outputFile    The PrintWriter to write the login activity to the output file.
 * @throws IOException if an error occurs during input/output operations.
 */
private void handleSuccessfulLogin(ActionEvent event, String usernameInput, PrintWriter outputFile) throws IOException, SQLException {
    AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    outputFile.print("user: " + usernameInput + " successfully logged in at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

    Appointments appointmentWithin15Min = findUpcomingAppointment();
    if (appointmentWithin15Min != null) {
        showAppointmentAlert("Appointment within 15 minutes: " + appointmentWithin15Min.getApmtId() + " and appointment start time of: " + appointmentWithin15Min.getApmtStart());
        System.out.println("There is an appointment within 15 minutes");
    } else {
        showAppointmentAlert("No upcoming appointments.");
        System.out.println("No upcoming appointments");
    }
}

    /**
     * Handles a failed login attempt.
     *
     * @param usernameInput The input username.
     * @param outputFile    The PrintWriter to write the login activity to the output file.
     */
    private void handleFailedLogin(String usernameInput, PrintWriter outputFile) {
        Locale locale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("resources/login", locale);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("error"));
        alert.setContentText(resources.getString("invalid"));
        alert.show();
        outputFile.print("user: " + usernameInput + " failed login attempt at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
    }

    /**
     * Displays an alert with the provided message.
     *
     * @param message The message to display in the alert.
     */
    private void showAppointmentAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        Optional<ButtonType> confirmation = alert.showAndWait();
    }

    /**
     * Retrieves the first appointment within 15 minutes from the current time.
     *
     * @return An Appointments object representing the first appointment within 15 minutes if found; otherwise, null.
     * @throws SQLException if there is an error while fetching appointments from the database.
     */
    private Appointments findUpcomingAppointment() throws SQLException {
        ObservableList<Appointments> getAllAppointments = AppointmentDB.getAllAppointments();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime currentTimePlus15Min = LocalDateTime.now().plusMinutes(15);

        return getAllAppointments.stream()
                .filter(appointment -> isWithin15Minutes(appointment, currentTimeMinus15Min, currentTimePlus15Min))
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if the given appointment is within 15 minutes of the current time.
     *
     * @param appointment         The appointment to check.
     * @param currentTimeMinus15Min The time 15 minutes before the current time.
     * @param currentTimePlus15Min  The time 15 minutes after the current time.
     * @return true if the appointment is within 15 minutes of the current time, false otherwise.
     */
    private boolean isWithin15Minutes(Appointments appointment, LocalDateTime currentTime, LocalDateTime currentTimePlus15Min) {
        LocalDateTime startTime = appointment.getApmtStart();
        return (startTime.isAfter(currentTime) || startTime.isEqual(currentTime)) && (startTime.isBefore(currentTimePlus15Min) || startTime.isEqual(currentTimePlus15Min));
    }

/**
 * Event handler
 * for the "Reset" button.
 * Clears the text fields for username and password inputs.
 *
 * @param event The ActionEvent triggered by the "Reset" button click.
 */
@FXML
void onReset(ActionEvent event) {
    UserField.setText("");
    PassField.setText("");
}

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is called automatically by JavaFX when the FXML file is loaded.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resource bundle used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Locale locale = Locale.getDefault();
            ResourceBundle resources = ResourceBundle.getBundle("resources/login", locale);
            ZoneId zone = ZoneId.systemDefault();
            SetTimeLabel.setText(zone.toString());

            LoginLabel.setText(resources.getString("header"));
            UserLabel.setText(resources.getString("username"));
            PassLabel.setText(resources.getString("password"));
            LoginButton.setText(resources.getString("login"));
            ExitButton.setText(resources.getString("exit"));
            TimeLabel.setText(resources.getString("location"));
            ResetButton.setText(resources.getString("reset"));

        } catch (MissingResourceException e) {
            System.out.println("Resource file missing: " + e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
