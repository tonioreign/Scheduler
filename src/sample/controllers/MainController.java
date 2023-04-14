package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.db.AppointmentDB;
import sample.misc.AccessMethod;
import sample.models.Appointments;
import sample.utils.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    /**
     * Button for generating a report in the UI.
     */
    @FXML
    private Button ReportButton;

    /**
     * Button for adding a new appointment in the UI.
     */
    @FXML
    private Button addAppointmentButton;

    /**
     * TableColumn for displaying the contact column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, Integer> apmtContactCol;

    /**
     * TableColumn for displaying the customer ID column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, Integer> apmtCustomerIdCol;

    /**
     * DatePicker for selecting the appointment date in the UI.
     */
    @FXML
    private DatePicker apmtDatePicker;

    /**
     * TableColumn for displaying the description column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, String> apmtDescriptionCol;

    /**
     * TableColumn for displaying the end date column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, LocalDateTime> apmtEndDateCol;

    /**
     * TableColumn for displaying the end time column in the appointments table view.
     */
    @FXML
    private TableColumn<?, ?> apmtEndTimeCol;

    /**
     * TableColumn for displaying the appointment ID column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, Integer> apmtIdCol;

    /**
     * TableColumn for displaying the location column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, String> apmtLocationCol;

    /**
     * TableColumn for displaying the start date column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, LocalDateTime> apmtStartDateCol;

    /**
     * TableColumn for displaying the start time column in the appointments table view.
     */
    @FXML
    private TableColumn<?, ?> apmtStartTimeCol;

    /**
     * TableView for displaying the list of appointments in the UI.
     */
    @FXML
    private TableView<Appointments> apmtTableView;

    /**
     * TableColumn for displaying the title column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, String> apmtTitleCol;

    /**
     * TableColumn for displaying the type column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, String> apmtTypeCol;

    /**
     * TableColumn for displaying the user ID column in the appointments table view.
     */
    @FXML
    private TableColumn<Appointments, Integer> apmtUserIdCol;

    /**
     * RadioButton for selecting the "by month" view option in the UI.
     */
    @FXML
    private RadioButton byMonthRadio;

    /**
     * RadioButton for selecting the "by week" view option in the UI.
     */
    @FXML
    private RadioButton byWeekRadio;

    /**
     * Button for logging out in the UI.
     */
    @FXML
    private Button logoutButton;

    /**
     * Button for modifying an appointment in the UI.
     */
    @FXML
    private Button modifyAppointmentButton;

    /**
     * Label for displaying the current time in the UI.
     */
    @FXML
    private Label setTimeLabel;

    /**
     * Label for displaying the time in the UI.
     */
    @FXML
    private Label timeLabel;

    /**
     * RadioButton for selecting the "view all" view option in the UI.
     */
    @FXML
    private RadioButton viewAllRadio;

    /**
     * Static variable for storing the selected appointment.
     */
    private static Appointments selectedAppointment;

    /**
     * Getter method for retrieving the selected appointment.
     *
     * @return The selected appointment.
     */
    public static Appointments getSelectedAppointment(){
        return selectedAppointment;
    }

    /**
     * Stage object for the UI.
     */
    private Stage stage;

    /**
     * Scene object for the UI.
     */
    private Scene scene;

    /**
     * Event handler for the "Add Appointment" button. Changes the screen to the "AddAppointment.fxml" view for scheduling a new appointment.
     *
     * @param event The ActionEvent triggered by clicking the "Add Appointment" button.
     * @throws IOException If an I/O error occurs while changing the screen.
     */
    @FXML
    void onAddApmt(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "AddAppointment.fxml", "Schedule Appointment");
    }

    /**
     * Event handler for the "Log Out" button. Changes the screen to the "login-form.fxml" view for logging out of the scheduling application.
     *
     * @param event The ActionEvent triggered by clicking the "Log Out" button.
     * @throws IOException If an I/O error occurs while changing the screen.
     */
    @FXML
    void onLogOut(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "login-form.fxml", "Scheduling Application");
    }

    /**
     * Event handler for the "Modify Appointment" button. Gets the selected appointment from the appointment table view and changes the screen to the "UpdateAppointment.fxml" view for updating the appointment.
     *
     * @param event The ActionEvent triggered by clicking the "Modify Appointment" button.
     * @throws IOException If an I/O error occurs while changing the screen.
     */
    @FXML
    void onModifyApmt(ActionEvent event) throws IOException {
        selectedAppointment = apmtTableView.getSelectionModel().getSelectedItem();
        AccessMethod.changeScreen(event, "UpdateAppointment.fxml", "Update Appointment");
    }

    /**
     * Event handler for the "Reports" button. Changes the screen to the "Reports.fxml" view for generating reports.
     *
     * @param event The ActionEvent triggered by clicking the "Reports" button.
     * @throws IOException If an I/O error occurs while changing the screen.
     */
    @FXML
    void onReports(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "Reports.fxml", "Reports");
    }

    /**
     * Event handler for the "View Customers" button. Changes the screen to the "ViewCustomers.fxml" view for viewing customer details.
     *
     * @param event The ActionEvent triggered by clicking the "View Customers" button.
     * @throws IOException If an I/O error occurs while changing the screen.
     */
    @FXML
    void onViewCustomers(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

    /**
     * Event handler for the "Delete Appointment" button. Deletes the selected appointment from the appointments table,
     * and refreshes the appointments table view.
     *
     * @param event The ActionEvent triggered by clicking the "Delete Appointment" button.
     * @throws Exception If an exception occurs while deleting the appointment or refreshing the appointments table view.
     */
    @FXML
    void deleteAppointment(ActionEvent event) throws Exception {
        try {
            Connection connection = DBConnection.openConnection();
            int deleteAppointmentID = apmtTableView.getSelectionModel().getSelectedItem().getApmtId();
            String deleteAppointmentType = apmtTableView.getSelectionModel().getSelectedItem().getApmtType();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected appointment with appointment id: " + deleteAppointmentID + " and appointment type " + deleteAppointmentType);
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                AppointmentDB.deleteAppointment(deleteAppointmentID, connection);

                ObservableList<Appointments> allAppointmentsList = AppointmentDB.getAllAppointments();
                apmtTableView.setItems(allAppointmentsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handler for the "View All" button. Displays all appointments in the appointments table view.
     *
     * @param event The ActionEvent triggered by clicking the "View All" button.
     * @throws SQLException If an exception occurs while retrieving appointments from the database.
     */
    @FXML
    void appointViewAllSelected(ActionEvent event) throws SQLException{
        try {
            byWeekRadio.setSelected(false);
            byMonthRadio.setSelected(false);
            ObservableList<Appointments> allAppointmentsList = AppointmentDB.getAllAppointments();

            if (allAppointmentsList != null)
                for (Appointments appointment : allAppointmentsList) {
                    apmtTableView.setItems(allAppointmentsList);
                }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Event handler for the "View by Month" button. Displays appointments for the current month in the appointments table view.
     *
     * @param event The ActionEvent triggered by clicking the "View by Month" button.
     * @throws SQLException If an exception occurs while retrieving appointments from the database.
     */
    @FXML
    void appointmentMonthSelected(ActionEvent event) throws SQLException {
        try {
            byWeekRadio.setSelected(false);
            viewAllRadio.setSelected(false);
            ObservableList<Appointments> allAppointmentsList = AppointmentDB.getAllAppointments();
            ObservableList<Appointments> appointmentsMonth = FXCollections.observableArrayList();

            LocalDateTime currentMonthStart = LocalDateTime.now().minusMonths(1);
            LocalDateTime currentMonthEnd = LocalDateTime.now().plusMonths(1);

            if (allAppointmentsList != null)

                allAppointmentsList.forEach(appointment -> {
                    if (appointment.getApmtEnd().isAfter(currentMonthStart) && appointment.getApmtEnd().isBefore(currentMonthEnd)) {
                        appointmentsMonth.add(appointment);
                    }
                    apmtTableView.setItems(appointmentsMonth);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handler for when an appointment week is selected.
     * It retrieves all appointments from the database and filters them
     * to only include appointments that fall within the past week.
     * It then sets the filtered appointments to be displayed in the apmtTableView table view.
     *
     * @param event (ActionEvent) The event triggered by the selection of an appointment week.
     * @throws SQLException If there is an error in retrieving appointments from the database.
     */
    @FXML
    void appointmentWeekSelected(ActionEvent event) throws SQLException {
        byMonthRadio.setSelected(false);
        viewAllRadio.setSelected(false);
        try {
            ObservableList<Appointments> allAppointmentsList = AppointmentDB.getAllAppointments();
            ObservableList<Appointments> appointmentsWeek = FXCollections.observableArrayList();

            LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
            LocalDateTime weekEnd = LocalDateTime.now().plusWeeks(1);

            if (allAppointmentsList != null)
                //IDE converted forEach
                allAppointmentsList.forEach(appointment -> {
                    if (appointment.getApmtEnd().isAfter(weekStart) && appointment.getApmtEnd().isBefore(weekEnd)) {
                        appointmentsWeek.add(appointment);
                    }
                    apmtTableView.setItems(appointmentsWeek);
                });
            else System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the view with data from the given URL and ResourceBundle. This method sets up
     * the display of appointments in a TableView, populating the columns with data from the
     * Appointments objects retrieved from the AppointmentDB database. It also sets the default
     * locale and time zone for the application.
     *
     * @param url The URL of the resource to initialize.
     * @param resourceBundle The ResourceBundle to use for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnection.openConnection();
        Locale locale = Locale.getDefault();
        Locale.setDefault(locale);

        ZoneId zone = ZoneId.systemDefault();
        setTimeLabel.setText(String.valueOf(zone));

        try {
            ObservableList<Appointments> allAppointments = AppointmentDB.getAllAppointments();

            apmtIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("apmtId"));
            apmtTitleCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("apmtTitle"));
            apmtDescriptionCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("apmtDescription"));
            apmtLocationCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("apmtLocation"));
            apmtTypeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("apmtType"));
            apmtStartDateCol.setCellValueFactory(new PropertyValueFactory<Appointments, LocalDateTime>("apmtStart"));
            apmtEndDateCol.setCellValueFactory(new PropertyValueFactory<Appointments, LocalDateTime>("apmtEnd"));
            apmtCustomerIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("apmtCustomerId"));
            apmtUserIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("apmtUserId"));
            apmtContactCol.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("apmtContactId"));

            apmtTableView.setItems(allAppointments);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
