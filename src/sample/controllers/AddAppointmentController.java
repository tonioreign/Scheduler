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
import java.util.ResourceBundle;

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
    Connection connection = DBConnection.openConnection();
    private ObservableList<Customer> getAllCustomers = CustomerDB.getAllCustomers(connection);
    private ObservableList<Integer> storeCustomerIDs = FXCollections.observableArrayList();
    private ObservableList<User> getAllUsers = UserDB.getAllUsers();
    private ObservableList<Integer> storeUserIDs = FXCollections.observableArrayList();
    private ObservableList<Appointments> getAllAppointments = AppointmentDB.getAllAppointments();


    private LocalDate localDateEnd = endDatePicker.getValue();
    private LocalDate localDateStart = startDatePicker.getValue();

    private DateTimeFormatter minHourFormat = DateTimeFormatter.ofPattern("HH:mm");
    private String appointmentStartDate = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private String appointmentStartTime = startTimeBox.getValue();

    private String endDate = endDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private String endTime = endTimeBox.getValue();

    private String startUTC = convertTimeDateUTC(appointmentStartDate + " " + appointmentStartTime + ":00");
    private String endUTC = convertTimeDateUTC(endDate + " " + endTime + ":00");

    private LocalTime localTimeStart = LocalTime.parse(startTimeBox.getValue(), minHourFormat);
    private LocalTime LocalTimeEnd = LocalTime.parse(endTimeBox.getValue(), minHourFormat);

    private LocalDateTime dateTimeStart = LocalDateTime.of(localDateStart, localTimeStart);
    private LocalDateTime dateTimeEnd = LocalDateTime.of(localDateEnd, LocalTimeEnd);

    private ZonedDateTime zoneDtStart = ZonedDateTime.of(dateTimeStart, ZoneId.systemDefault());
    private ZonedDateTime zoneDtEnd = ZonedDateTime.of(dateTimeEnd, ZoneId.systemDefault());

    private ZonedDateTime convertStartEST = zoneDtStart.withZoneSameInstant(ZoneId.of("America/New_York"));
    private ZonedDateTime convertEndEST = zoneDtEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

    private LocalTime startAppointmentTimeToCheck = convertStartEST.toLocalTime();
    private LocalTime endAppointmentTimeToCheck = convertEndEST.toLocalTime();

    private DayOfWeek startAppointmentDayToCheck = convertStartEST.toLocalDate().getDayOfWeek();
    private DayOfWeek endAppointmentDayToCheck = convertEndEST.toLocalDate().getDayOfWeek();

    public AddAppointmentController() throws SQLException {
    }


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
        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
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
        try {

            if (!titleField.getText().isEmpty() && !descField.getText().isEmpty() && !locationField.getText().isEmpty() && !typeField.getText().isEmpty() && startDatePicker.getValue() != null && endDatePicker.getValue() != null && !startTimeBox.getValue().isEmpty() && !endTimeBox.getValue().isEmpty() && !(customerIDBox.getValue() == null)) {

                getAllCustomers.stream().map(Customer::getCustomerID).forEach(storeCustomerIDs::add);
                getAllUsers.stream().map(User::getUserId).forEach(storeUserIDs::add);

                int startAppointmentDayToCheckInt = startAppointmentDayToCheck.getValue();
                int endAppointmentDayToCheckInt = endAppointmentDayToCheck.getValue();

                int workWeekStart = DayOfWeek.MONDAY.getValue();
                int workWeekEnd = DayOfWeek.FRIDAY.getValue();

                LocalTime estBusinessStart = LocalTime.of(8, 0, 0);
                LocalTime estBusinessEnd = LocalTime.of(22, 0, 0);

                if (startAppointmentDayToCheckInt < workWeekStart || startAppointmentDayToCheckInt > workWeekEnd || endAppointmentDayToCheckInt < workWeekStart || endAppointmentDayToCheckInt > workWeekEnd) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Day is outside of business operations (Monday-Friday)");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    System.out.println("day is outside of business hours");
                    return;
                }

                if (startAppointmentTimeToCheck.isBefore(estBusinessStart) || startAppointmentTimeToCheck.isAfter(estBusinessEnd) || endAppointmentTimeToCheck.isBefore(estBusinessStart) || endAppointmentTimeToCheck.isAfter(estBusinessEnd)) {
                    System.out.println("time is outside of business hours");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Time is outside of business hours (8am-10pm EST): " + startAppointmentTimeToCheck + " - " + endAppointmentTimeToCheck + " EST");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                int newAppointmentID = Integer.parseInt(String.valueOf((int) (Math.random() * 100)));
                int customerID = customerIDBox.getValue();

                if (dateTimeStart.isAfter(dateTimeEnd)) {
                    System.out.println("Appointment has start time after end time");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment has start time after end time");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                if (dateTimeStart.isEqual(dateTimeEnd)) {
                    System.out.println("Appointment has same start and end time");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment has same start and end time");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }
                for (Appointments appointment : getAllAppointments) {
                    LocalDateTime checkStart = appointment.getApmtStart();
                    LocalDateTime checkEnd = appointment.getApmtEnd();

                    //"outer verify" meaning check to see if an appointment exists between start and end.
                    if ((customerID == appointment.getApmtCustomerId()) && (newAppointmentID != appointment.getApmtId()) &&
                            (dateTimeStart.isBefore(checkStart)) && (dateTimeEnd.isAfter(checkEnd))) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment overlaps with existing appointment.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("Appointment overlaps with existing appointment.");
                        return;
                    }

                    if ((customerID == appointment.getApmtCustomerId()) && (newAppointmentID != appointment.getApmtId()) &&
                            (dateTimeStart.isAfter(checkStart)) && (dateTimeStart.isBefore(checkEnd))) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Start time overlaps with existing appointment.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("Start time overlaps with existing appointment.");
                        return;
                    }


                    if (customerID == appointment.getApmtCustomerId() && (newAppointmentID != appointment.getApmtId()) &&
                            (dateTimeEnd.isAfter(checkStart)) && (dateTimeEnd.isBefore(checkEnd))) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "End time overlaps with existing appointment.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("End time overlaps with existing appointment.");
                        return;
                    }
                }

                String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, User_ID, Customer_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                Connection conn = DBConnection.openConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, newAppointmentID);
                ps.setString(2, titleField.getText());
                ps.setString(3, descField.getText());
                ps.setString(4, locationField.getText());
                ps.setString(5, typeField.getText());
                ps.setTimestamp(6, Timestamp.valueOf(startUTC));
                ps.setTimestamp(7, Timestamp.valueOf(endUTC));
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(11, "admin");
                ps.setInt(12, userIDBox.getValue());
                ps.setInt(13, customerID);
                ps.setInt(14, contactBox.getValue());

                ps.executeUpdate();
            }
            else {
                System.out.println("Something is wrong");
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Integer> allContactIDs = FXCollections.observableArrayList();
        ObservableList<Integer> allCustomerIDs = FXCollections.observableArrayList();
        ObservableList<Integer> allUserIDs = FXCollections.observableArrayList();

        // lambda #2
        contactsList.forEach(contacts -> allContactIDs.add(contacts.getContactID()));
        customerIDList.forEach(customer -> allCustomerIDs.add(customer.getCustomerID()));
        userIDsList.forEach(user -> allUserIDs.add(user.getUserId()));

        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
        LocalTime firstAppointment = LocalTime.MIN.plusHours(8);
        LocalTime lastAppointment = LocalTime.MAX.minusHours(1).minusMinutes(45);

        //if statement fixed issue with infinite loop
        if (!firstAppointment.equals(0) || !lastAppointment.equals(0)) {
            while (firstAppointment.isBefore(lastAppointment)) {
                appointmentTimes.add(String.valueOf(firstAppointment));
                firstAppointment = firstAppointment.plusMinutes(15);
            }
        }
        startTimeBox.setItems(appointmentTimes);
        endTimeBox.setItems(appointmentTimes);
        contactBox.setItems(allContactIDs);
        customerIDBox.setItems(allCustomerIDs);
        userIDBox.setItems(allUserIDs);
    }
}