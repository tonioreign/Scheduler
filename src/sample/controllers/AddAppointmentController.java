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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static sample.utils.TimeZoneUtil.convertTimeDateUTC;

public class AddAppointmentController implements Initializable {

    /**
     * Text field for the title input.
     */
    @FXML
    private TextField titleField;

    /**
     * Text field for the description input.
     */
    @FXML
    private TextField descField;

    /**
     * Text field for the location input.
     */
    @FXML
    private TextField locationField;

    /**
     * Combo box for selecting a contact ID.
     */
    @FXML
    private ComboBox<Integer> contactBox;

    /**
     * Text field for the type input.
     */
    @FXML
    private TextField typeField;

    /**
     * Date picker for selecting the start date.
     */
    @FXML
    private DatePicker startDatePicker;

    /**
     * Combo box for selecting the start time.
     */
    @FXML
    private ComboBox<String> startTimeBox;

    /**
     * Date picker for selecting the end date.
     */
    @FXML
    private DatePicker endDatePicker;

    /**
     * Combo box for selecting the end time.
     */
    @FXML
    private ComboBox<String> endTimeBox;

    /**
     * Combo box for selecting a customer ID.
     */
    @FXML
    private ComboBox<Integer> customerIDBox;

    /**
     * Combo box for selecting a user ID.
     */
    @FXML
    private ComboBox<Integer> userIDBox;


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
        alert.setHeaderText("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
        }
    }

    /**
     * Event handler for the "Save" button in a JavaFX application.
     * This method is called when the "Save" button is clicked.
     * It performs various validation checks on the input fields and saves the appointment details to the database if all checks pass.
     * @param event The ActionEvent object representing the button click event.
     * @throws IOException If an IO exception occurs while performing database operations.
     */
    @FXML
    void onSave(ActionEvent event) throws IOException {
        Integer newAppointmentID = new Random().nextInt(10000);
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
            int contactID = contactBox.getValue();
            int customerID = customerIDBox.getValue();
            int userID = userIDBox.getValue();

            LocalDate localDateEnd = endDatePicker.getValue();
            LocalDate localDateStart = startDatePicker.getValue();

            DateTimeFormatter minHourFormat = DateTimeFormatter.ofPattern("HH:mm");
            String appointmentStartDate = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String appointmentStartTime = startTimeBox.getValue();

            String endDate = endDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String endTime = endTimeBox.getValue();

            System.out.println("thisDate + thisStart " + appointmentStartDate + " " + appointmentStartTime + ":00");
            String startUTC = convertTimeDateUTC(appointmentStartDate + " " + appointmentStartTime + ":00");
            String endUTC = convertTimeDateUTC(endDate + " " + endTime + ":00");

            LocalTime localTimeStart = LocalTime.parse(startTimeBox.getValue(), minHourFormat);
            LocalTime localTimeEnd = LocalTime.parse(endTimeBox.getValue(), minHourFormat);

            LocalDateTime dateTimeStart = LocalDateTime.of(localDateStart, localTimeStart);
            LocalDateTime dateTimeEnd = LocalDateTime.of(localDateEnd, localTimeEnd);

            ZonedDateTime zoneDtStart = ZonedDateTime.of(dateTimeStart, ZoneId.systemDefault());
            ZonedDateTime zoneDtEnd = ZonedDateTime.of(dateTimeEnd, ZoneId.systemDefault());

            ZonedDateTime convertStartEST = zoneDtStart.withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime convertEndEST = zoneDtEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

            LocalTime startAppointmentTimeToCheck = convertStartEST.toLocalTime();
            LocalTime endAppointmentTimeToCheck = convertEndEST.toLocalTime();

            DayOfWeek startAppointmentDayToCheck = convertStartEST.getDayOfWeek();
            DayOfWeek endAppointmentDayToCheck = convertEndEST.getDayOfWeek();

            LocalTime estBusinessStart = LocalTime.of(8, 0, 0);
            LocalTime estBusinessEnd = LocalTime.of(22, 0, 0);

            if (startAppointmentTimeToCheck.isBefore(estBusinessStart) || startAppointmentTimeToCheck.isAfter(estBusinessEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments must be scheduled between 8:00 AM and 10:00 PM EST.");
                alert.showAndWait();
            } else if (endAppointmentTimeToCheck.isBefore(estBusinessStart) || endAppointmentTimeToCheck.isAfter(estBusinessEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments must be scheduled between 8:00 AM and 10:00 PM EST.");
                alert.showAndWait();
            } else if (startAppointmentDayToCheck == DayOfWeek.SATURDAY || startAppointmentDayToCheck == DayOfWeek.SUNDAY) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments cannot be scheduled on weekends.");
                alert.showAndWait();
            } else if (endAppointmentDayToCheck == DayOfWeek.SATURDAY || endAppointmentDayToCheck == DayOfWeek.SUNDAY) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments cannot be scheduled on weekends.");
                alert.showAndWait();
            } else if (startAppointmentTimeToCheck.isAfter(endAppointmentTimeToCheck)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start date/time must be before end date/time.");
                alert.showAndWait();
            } else if (dateTimeStart.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start date/time must be in the future.");
                alert.showAndWait();
            } else if (AppointmentDB.checkForAppointmentOverlap(dateTimeStart, dateTimeEnd, userID)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You already have an appointment scheduled during this time.");
                alert.showAndWait();
            } else if (AppointmentDB.checkForAppointmentOverlap(dateTimeStart, dateTimeEnd, customerID)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "This customer already has an appointment scheduled during this time.");
                alert.showAndWait();
            } else {
                try (Connection conn = DBConnection.openConnection()) {
                    String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, User_ID, Customer_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, newAppointmentID);
                    ps.setString(2, title);
                    ps.setString(3, description);
                    ps.setString(4, location);
                    ps.setString(5, type);
                    ps.setTimestamp(6, Timestamp.valueOf(startUTC));
                    ps.setTimestamp(7, Timestamp.valueOf(endUTC));
                    ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(9, "admin");
                    ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(11, "admin");
                    ps.setInt(12, userID);
                    ps.setInt(13, customerID);
                    ps.setInt(14, contactID);
                    ps.execute();
                    ps.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment successfully added.");
                    alert.showAndWait();
                    AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public LocalDateTime convertUTCToLocal(String utcDateTime) {
        LocalDateTime utcTime = LocalDateTime.parse(utcDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ZonedDateTime utcZoneDateTime = ZonedDateTime.of(utcTime, ZoneId.of("UTC"));
        ZonedDateTime localZoneDateTime = utcZoneDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return localZoneDateTime.toLocalDateTime();
    }

    /**
     * Overrides the initialize method from the Initializable interface to initialize the UI elements and data
     * when the associated FXML file is loaded.
     *
     * @param url            The URL location of the FXML file.
     * @param resourceBundle The ResourceBundle associated with the FXML file.
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
            ObservableList<Appointments> appointments = AppointmentDB.getAllAppointments();

            for (Appointments appointment : appointments) {
                LocalDateTime localStart = convertUTCToLocal(appointment.getApmtStart());
                LocalDateTime localEnd = convertUTCToLocal(appointment.getApmtEnd());
                appointment.setApmtStart(LocalDateTime.parse(localStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                appointment.setApmtStart(LocalDateTime.parse(localEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> allContactIDs = FXCollections.observableArrayList();
        ObservableList<Integer> allCustomerIDs = FXCollections.observableArrayList();
        ObservableList<Integer> allUserIDs = FXCollections.observableArrayList();

        // lambda #1
        contactsList.forEach(contact -> allContactIDs.add(contact.getContactID()));
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
    }
}