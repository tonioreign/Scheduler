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

    @FXML
    private TextField titleField;

    @FXML
    private TextField descField;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<Integer> contactBox;

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

            if (!titleField.getText().isEmpty() && !descField.getText().isEmpty() && !locationField.getText().isEmpty() && !typeField.getText().isEmpty() && startDatePicker.getValue() != null && endDatePicker.getValue() != null && !startTimeBox.getValue().isEmpty() && !endTimeBox.getValue().isEmpty() && !(customerIDBox.getValue() == null)) {

                ObservableList<Customer> allCustomers = CustomerDB.getAllCustomers(connection);
                ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
                ObservableList<User> allUsers = UserDB.getAllUsers();
                ObservableList<Integer> userIDs = FXCollections.observableArrayList();
                ObservableList<Appointments> allAppointments = AppointmentDB.getAllAppointments();


                allCustomers.stream().map(Customer::getCustomerID).forEach(customerIDs::add);
                allUsers.stream().map(User::getUserId).forEach(userIDs::add);

                LocalDate localDateEnd = endDatePicker.getValue();
                LocalDate localDateStart = startDatePicker.getValue();

                DateTimeFormatter minHourFormat = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String appointmentStartDate = localDateStart.format(dateFormatter);
                String appointmentStartTime = startTimeBox.getValue();
                String endDate = localDateEnd.format(dateFormatter);
                String endTime = endTimeBox.getValue();

                String startDateTime = appointmentStartDate + " " + appointmentStartTime + ":00";
                String endDateTime = endDate + " " + endTime + ":00";

                String startUTC = convertTimeDateUTC(startDateTime);
                String endUTC = convertTimeDateUTC(endDateTime);

                LocalDateTime dateTimeStart = LocalDateTime.parse(startDateTime, dateFormatter);
                LocalDateTime dateTimeEnd = LocalDateTime.parse(endDateTime, dateFormatter);

                ZoneId systemZone = ZoneId.systemDefault();
                ZoneId targetZone = ZoneId.of("America/New_York");

                ZonedDateTime zoneDtStart = dateTimeStart.atZone(systemZone);
                ZonedDateTime zoneDtEnd = dateTimeEnd.atZone(systemZone);

                ZonedDateTime convertStartEST = zoneDtStart.withZoneSameInstant(targetZone);
                ZonedDateTime convertEndEST = zoneDtEnd.withZoneSameInstant(targetZone);

                LocalTime startAppointmentTimeToCheck = convertStartEST.toLocalTime();
                LocalTime endAppointmentTimeToCheck = convertEndEST.toLocalTime();

                DayOfWeek startAppointmentDayToCheck = convertStartEST.getDayOfWeek();
                DayOfWeek endAppointmentDayToCheck = convertEndEST.getDayOfWeek();

                int startAppointmentDayToCheckInt = startAppointmentDayToCheck.getValue();
                int endAppointmentDayToCheckInt = endAppointmentDayToCheck.getValue();

                int workWeekStart = DayOfWeek.MONDAY.getValue();
                int workWeekEnd = DayOfWeek.FRIDAY.getValue();

                LocalTime estBusinessStart = LocalTime.of(8, 0, 0);
                LocalTime estBusinessEnd = LocalTime.of(22, 0, 0);

                checkBusinessHours(convertStartEST, convertEndEST, estBusinessStart, estBusinessEnd, workWeekStart, workWeekEnd);

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
                for (Appointments appointment : allAppointments) {
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
                Connection conn = DBConnection.getConnection();
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
                ps.setInt(12, customerID);
                ps.setInt(13, userIDBox.getValue());
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

    public void checkBusinessHours(ZonedDateTime convertStartEST, ZonedDateTime convertEndEST, LocalTime estBusinessStart, LocalTime estBusinessEnd, int workWeekStart, int workWeekEnd) {
        DayOfWeek startAppointmentDayToCheck = convertStartEST.getDayOfWeek();
        DayOfWeek endAppointmentDayToCheck = convertEndEST.getDayOfWeek();

        int startAppointmentDayToCheckInt = startAppointmentDayToCheck.getValue();
        int endAppointmentDayToCheckInt = endAppointmentDayToCheck.getValue();

        if (startAppointmentDayToCheckInt < workWeekStart || startAppointmentDayToCheckInt > workWeekEnd || endAppointmentDayToCheckInt < workWeekStart || endAppointmentDayToCheckInt > workWeekEnd) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Day is outside of business operations (Monday-Friday)");
            Optional<ButtonType> confirmation = alert.showAndWait();
            System.out.println("day is outside of business hours");
            return;
        }

        LocalTime startAppointmentTimeToCheck = convertStartEST.toLocalTime();
        LocalTime endAppointmentTimeToCheck = convertEndEST.toLocalTime();

        if (startAppointmentTimeToCheck.isBefore(estBusinessStart) || startAppointmentTimeToCheck.isAfter(estBusinessEnd) || endAppointmentTimeToCheck.isBefore(estBusinessStart) || endAppointmentTimeToCheck.isAfter(estBusinessEnd)) {
            System.out.println("time is outside of business hours");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Time is outside of business hours (8am-10pm EST): " + startAppointmentTimeToCheck + " - " + endAppointmentTimeToCheck + " EST");
            Optional<ButtonType> confirmation = alert.showAndWait();
            return;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contacts> contactsList = null;
        ObservableList<Customer> customerIDList = null;
        ObservableList<User> userIDsList = null;
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
        // here
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
        customerIDBox.setItems(allUserIDs);
        userIDBox.setItems(allCustomerIDs);
    }
}