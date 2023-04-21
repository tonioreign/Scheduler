package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.db.AppointmentDB;
import sample.db.ContactDB;
import sample.db.CustomerDB;
import sample.db.UserDB;
import sample.misc.AccessMethod;
import sample.models.Appointments;
import sample.models.Contacts;
import sample.models.Customer;
import sample.models.User;
import sample.utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controller class for the UpdateAppointment screen.
 * This class contains the event handlers for the UpdateAppointment screen.
 *
 * @author Antonio Jenkins
 */
public class UpdateAppointmentController implements Initializable {

    /**
     * TextField for entering appointment title in the UI.
     */
    @FXML
    private TextField titleField;

    /**
     * TextField for entering appointment description in the UI.
     */
    @FXML
    private TextField descField;

    /**
     * TextField for entering appointment location in the UI.
     */
    @FXML
    private TextField locationField;

    /**
     * ComboBox for selecting appointment contact in the UI.
     */
    @FXML
    private ComboBox<Integer> contactBox;

    /**
     * TextField for entering appointment type in the UI.
     */
    @FXML
    private TextField typeField;

    /**
     * DatePicker for selecting appointment start date in the UI.
     */
    @FXML
    private DatePicker startDatePicker;

    /**
     * ComboBox for selecting appointment start time in the UI.
     */
    @FXML
    private ComboBox<String> startTimeBox;

    /**
     * DatePicker for selecting appointment end date in the UI.
     */
    @FXML
    private DatePicker endDatePicker;

    /**
     * ComboBox for selecting appointment end time in the UI.
     */
    @FXML
    private ComboBox<String> endTimeBox;

    /**
     * ComboBox for selecting customer ID in the UI.
     */
    @FXML
    private ComboBox<Integer> customerIDBox;

    /**
     * ComboBox for selecting user ID in the UI.
     */
    @FXML
    private ComboBox<Integer> userIDBox;

    private Appointments selectedAppointment;

    /**
     * Event handler for the cancel button
     * Back to the main screen of the application
     *
     * @param event The ActionEvent object that triggered the event.
     *              This is typically provided by the JavaFX framework automatically
     *              when the event handler is invoked.
     * @throws IOException If an I/O exception occurs during the screen change process.
     *                     This exception is thrown by the AccessMethod.changeScreen() method,
     *                     which is responsible for changing the screen or scene in the JavaFX application.
     */
    @FXML
    void onCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Any unsaved changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
        }
    }

    /**
     * Updates an appointment when the "Save" button is clicked.
     * @param event The ActionEvent object that represents the event of the "Save" button being clicked.
     * @throws IOException If an I/O error occurs while opening the database connection.
     */
    @FXML
    void onSave(ActionEvent event) throws IOException {
        Integer newAppointmentID = new Random().nextInt(100);
        if (titleField.getText().isBlank() || descField.getText().isBlank() || locationField.getText().isBlank() ||
                typeField.getText().isBlank() || startDatePicker.getValue() == null || startTimeBox.getSelectionModel().isEmpty() ||
                endDatePicker.getValue() == null || endTimeBox.getSelectionModel().isEmpty() || contactBox.getSelectionModel().isEmpty() ||
                customerIDBox.getSelectionModel().isEmpty() || userIDBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
            alert.showAndWait();
        } else {
            String title = titleField.getText();
            String description = descField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String startTime = startTimeBox.getValue();
            String endTime = endTimeBox.getValue();
            int contactID = contactBox.getValue();
            int customerID = customerIDBox.getValue();
            int userID = userIDBox.getValue();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String startDateString = startDate + " " + startTime;
            String endDateString = endDate + " " + endTime;
            LocalDateTime startDateTime = LocalDateTime.parse(startDateString, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDateString, formatter);

            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime startZonedDateTime = startDateTime.atZone(zoneId);
            ZonedDateTime endZonedDateTime = endDateTime.atZone(zoneId);
            ZonedDateTime startEST = startZonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime endEST = endZonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

            if (startEST.getDayOfWeek() == DayOfWeek.SATURDAY || startEST.getDayOfWeek() == DayOfWeek.SUNDAY) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments cannot be scheduled on weekends.");
                alert.showAndWait();
            }else if (endEST.getDayOfWeek() == DayOfWeek.SATURDAY || endEST.getDayOfWeek() == DayOfWeek.SUNDAY) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments cannot be scheduled on weekends.");
                alert.showAndWait();
            } else if (startDateTime.isAfter(endDateTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start date/time must be before end date/time.");
                alert.showAndWait();
            } else if (startDateTime.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start date/time must be in the future.");
                alert.showAndWait();
            } else if (AppointmentDB.checkForAppointmentOverlap(startDateTime, endDateTime, customerIDBox.getValue(), contactBox.getValue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "This customer already has an appointment with the same contact during this time.");
                alert.showAndWait();
            } else if (AppointmentDB.checkForAppointmentOverlapping(startDateTime, endDateTime, customerIDBox.getValue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "This customer already has an appointment during this time.");
                alert.showAndWait();
            } else {
                ZonedDateTime utcStartZDT = startZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
                ZonedDateTime utcEndZDT = endZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
                LocalDateTime utcStartDateTime = utcStartZDT.toLocalDateTime();
                LocalDateTime utcEndDateTime = utcEndZDT.toLocalDateTime();
                Timestamp startTimestamp = Timestamp.valueOf(utcStartDateTime);
                Timestamp endTimestamp = Timestamp.valueOf(utcEndDateTime);

                try {
                    Connection connection = DBConnection.openConnection();
                    String sql = "UPDATE appointments SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
                    Connection conn = DBConnection.openConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, selectedAppointment.getApmtId());
                    ps.setString(2, title);
                    ps.setString(3, description);
                    ps.setString(4, location);
                    ps.setString(5, type);
                    ps.setTimestamp(6, Timestamp.valueOf(startTimestamp.toLocalDateTime()));
                    ps.setTimestamp(7, Timestamp.valueOf(endTimestamp.toLocalDateTime()));
                    ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(9, "admin");
                    ps.setInt(10, customerID);
                    ps.setInt(11, userID);
                    ps.setInt(12, contactID);
                    ps.setInt(13, selectedAppointment.getApmtId());
                    ps.execute();
                    ps.close();
                    connection.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment successfully updated.");
                    alert.showAndWait();
                    AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    /**
     * Initializes the UI components with data for editing an appointment.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contacts> contactsList = FXCollections.observableArrayList();
        ObservableList<Customer> customerIDList = FXCollections.observableArrayList();
        ObservableList<User> userIDsList = FXCollections.observableArrayList();
        Connection connection = DBConnection.openConnection();
        try {
            contactsList = ContactDB.getAllContacts();
            customerIDList = CustomerDB.getAllCustomers(connection);
            userIDsList = UserDB.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> allContactIDs = FXCollections.observableArrayList();
        ObservableList<Integer> allCustomerIDs = FXCollections.observableArrayList();
        ObservableList<Integer> allUserIDs = FXCollections.observableArrayList();

        // lambda #1
        contactsList.forEach(contacts -> allContactIDs.add(contacts.getContactID()));
        customerIDList.forEach(customer -> allCustomerIDs.add(customer.getCustomerID()));
        userIDsList.forEach(user -> allUserIDs.add(user.getUserId()));

        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
        LocalTime firstAppointment = LocalTime.MIN.plusHours(8);
        LocalTime lastAppointment = LocalTime.MAX.minusHours(1).minusMinutes(45);

        // generates a list of appointment times in 15 minutes intervals between firstAppointment and lastAppointment
        while (firstAppointment.isBefore(lastAppointment)) {
            appointmentTimes.add(firstAppointment.toString());
            firstAppointment = firstAppointment.plusMinutes(15);
        }

        startTimeBox.setItems(appointmentTimes);
        endTimeBox.setItems(appointmentTimes);
        contactBox.setItems(allContactIDs);
        customerIDBox.setItems(allCustomerIDs);
        userIDBox.setItems(allUserIDs);

        selectedAppointment = MainController.getSelectedAppointment();
        if (selectedAppointment != null) {

            titleField.setText(selectedAppointment.getApmtTitle());
            descField.setText(selectedAppointment.getApmtDescription());
            locationField.setText(selectedAppointment.getApmtLocation());
            typeField.setText(selectedAppointment.getApmtType());
            customerIDBox.setValue(selectedAppointment.getApmtCustomerId());
            startDatePicker.setValue(selectedAppointment.getApmtStart().toLocalDate());
            endDatePicker.setValue(selectedAppointment.getApmtEnd().toLocalDate());
            startTimeBox.setValue(String.valueOf(selectedAppointment.getApmtStart().toLocalTime()));
            endTimeBox.setValue(String.valueOf(selectedAppointment.getApmtEnd().toLocalTime()));
            userIDBox.setValue(selectedAppointment.getApmtUserId());
            contactBox.setValue(selectedAppointment.getApmtContactId());
        }

    }
}