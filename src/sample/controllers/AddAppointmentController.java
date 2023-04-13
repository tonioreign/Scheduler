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
import java.util.*;
import java.util.stream.Collectors;

import static sample.utils.TimeZoneUtil.convertTimeDateUTC;

public class AddAppointmentController implements Initializable {

    @FXML
    private TextField titleField;

    @FXML
    private TextField descField;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> contactBox;

    @FXML
    private TextField typeField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> startTimeBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> endTimeBox;

    @FXML
    private ComboBox<Integer> customerIDBox;

    @FXML
    private ComboBox<Integer> userIDBox;

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    }

    @FXML
    void onSave(ActionEvent event) throws IOException {
        try {

            Connection connection = DBConnection.openConnection();

            if (!titleField.getText().isEmpty() && !descField.getText().isEmpty() && !locationField.getText().isEmpty() && !typeField.getText().isEmpty() && startDatePicker.getValue() != null && endDatePicker.getValue() != null && !startTimeBox.getValue().isEmpty() && !endTimeBox.getValue().isEmpty() && customerIDBox.getValue() == null) {

                ObservableList<Customer> getAllCustomers = CustomerDB.getAllCustomers(connection);
                ObservableList<Integer> storeCustomerIDs = FXCollections.observableArrayList();
                ObservableList<User> getAllUsers = UserDB.getAllUsers();
                ObservableList<Integer> storeUserIDs = FXCollections.observableArrayList();
                ObservableList<Appointments> getAllAppointments = AppointmentDB.getAllAppointments();

                DateTimeFormatter minHourFormat = DateTimeFormatter.ofPattern("HH:mm");
                String appointmentStartDate = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String appointmentStartTime = startTimeBox.getValue();


                storeCustomerIDs.addAll(getAllCustomers.stream().map(Customer::getCustomerID).collect(Collectors.toList()));
                storeUserIDs.addAll(getAllUsers.stream().map(User::getUserId).collect(Collectors.toList()));

                LocalDate localDateStart = startDatePicker.getValue();
                LocalDate localDateEnd = endDatePicker.getValue();


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

                ZoneId estZoneId = ZoneId.of("America/New_York");
                LocalTime startAppointmentTimeToCheck = zoneDtStart.withZoneSameInstant(estZoneId).toLocalTime();
                LocalTime endAppointmentTimeToCheck = zoneDtEnd.withZoneSameInstant(estZoneId).toLocalTime();

                DayOfWeek startAppointmentDayToCheck = zoneDtStart.withZoneSameInstant(estZoneId).toLocalDate().getDayOfWeek();
                DayOfWeek endAppointmentDayToCheck = zoneDtEnd.withZoneSameInstant(estZoneId).toLocalDate().getDayOfWeek();


                int workWeekStart = DayOfWeek.MONDAY.getValue();
                int workWeekEnd = DayOfWeek.FRIDAY.getValue();

                LocalTime estBusinessStart = LocalTime.of(8, 0, 0);
                LocalTime estBusinessEnd = LocalTime.of(22, 0, 0);

                List<DayOfWeek> daysToCheck = Arrays.asList(startAppointmentDayToCheck, endAppointmentDayToCheck);
                if (daysToCheck.stream().anyMatch(day -> day.getValue() < workWeekStart || day.getValue() > workWeekEnd)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Day is outside of business operations (Monday-Friday)");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    System.out.println("Day is outside of business hours");
                    return;
                }


                if (startAppointmentTimeToCheck.isBefore(estBusinessStart) || startAppointmentTimeToCheck.isAfter(estBusinessEnd)
                        || endAppointmentTimeToCheck.isBefore(estBusinessStart) || endAppointmentTimeToCheck.isAfter(estBusinessEnd)) {
                    System.out.println("Time is outside of business hours");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Time is outside of business hours (8am-10pm EST): " +
                            startAppointmentTimeToCheck + " - " + endAppointmentTimeToCheck + " EST");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                Random random = new Random();
                int newAppointmentID = random.nextInt(100); // generates a random integer between 0 (inclusive) and 100 (exclusive)

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

                    // "outer verify" meaning check to see if an appointment exists between start and end.
                    if ((customerID == appointment.getApmtCustomerId()) && (newAppointmentID != appointment.getApmtId()) &&
                            (dateTimeStart.isBefore(checkEnd)) && (dateTimeEnd.isAfter(checkStart))) {
                        // Changed condition to check if the start time of the new appointment is before the end time of the existing appointment,
                        // and if the end time of the new appointment is after the start time of the existing appointment
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment overlaps with existing appointment.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("Appointment overlaps with existing appointment.");
                        return;
                    }

                    if ((customerID == appointment.getApmtCustomerId()) && (newAppointmentID != appointment.getApmtId()) &&
                            (dateTimeStart.isAfter(checkStart)) && (dateTimeStart.isBefore(checkEnd))) {
                        // Changed condition to check if the start time of the new appointment is after the start time of the existing appointment,
                        // and if the start time of the new appointment is before the end time of the existing appointment
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Start time overlaps with existing appointment.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("Start time overlaps with existing appointment.");
                        return;
                    }

                    if (customerID == appointment.getApmtCustomerId() && (newAppointmentID != appointment.getApmtId()) &&
                            (dateTimeEnd.isAfter(checkStart)) && (dateTimeEnd.isBefore(checkEnd))) {
                        // Changed condition to check if the end time of the new appointment is after the start time of the existing appointment,
                        // and if the end time of the new appointment is before the end time of the existing appointment
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "End time overlaps with existing appointment.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        System.out.println("End time overlaps with existing appointment.");
                        return;
                    }
                }


                String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, User_ID, Customer_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, newAppointmentID);
                ps.setString(2, titleField.getText());
                ps.setString(3, descField.getText());
                ps.setString(4, locationField.getText());
                ps.setString(5, typeField.getText());
                //ps.setTimestamp(6, Timestamp.valueOf(startLocalDateTimeToAdd));
                ps.setTimestamp(6, Timestamp.valueOf(startUTC));
                ps.setTimestamp(7, Timestamp.valueOf(endUTC));
                //need to verify this is correct
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(11, customerID);
                ps.setInt(12, userIDBox.getValue());
                ps.setInt(13, Integer.parseInt(contactBox.getValue()));

                ps.executeUpdate();
            }
        }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    }

        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contacts> contactsList = null;
        ObservableList<Customer> customerIDList = null;
        ObservableList<User> userIDsList = null;
        try (Connection connection = DBConnection.getConnection()) {
            contactsList = ContactDB.getAllContacts();
            customerIDList = CustomerDB.getAllCustomers(connection);
            userIDsList = UserDB.getAllUsers();
        } catch (SQLException e) {
            // Handle the exception appropriately, e.g., log it, show an error message, etc.
            e.printStackTrace();
            // Or, propagate the exception to the caller for handling
            throw new RuntimeException("Failed to retrieve data from the database", e);
        }


        // Using Java stream to collect contact names, customer IDs, and user IDs
        ObservableList<String> allContactNames = (ObservableList<String>) contactsList.stream()
                .map(Contacts::getContactName)
                .collect(Collectors.toList());

        ObservableList<Integer> allCustomerIDs = (ObservableList<Integer>) customerIDList.stream()
                .map(Customer::getCustomerID)
                .collect(Collectors.toList());

        ObservableList<Integer> allUserIDs = (ObservableList<Integer>) userIDsList.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());

        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

        LocalTime firstAppointment = LocalTime.MIN.plusHours(8);
        LocalTime lastAppointment = LocalTime.MAX.minusHours(1).minusMinutes(45);

        if (firstAppointment != null && lastAppointment != null) {
            while (firstAppointment.isBefore(lastAppointment)) {
                appointmentTimes.add(firstAppointment.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                firstAppointment = firstAppointment.plusMinutes(15);
            }
        }

        startTimeBox.setItems(appointmentTimes);
        endTimeBox.setItems(appointmentTimes);
        contactBox.setItems(allContactNames);
        customerIDBox.setItems(allUserIDs);
        userIDBox.setItems(allCustomerIDs);
    }
}
