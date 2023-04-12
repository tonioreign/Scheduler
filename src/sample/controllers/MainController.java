package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.db.AppointmentDB;
import sample.misc.AccessMethod;
import sample.models.Appointments;
import sample.utils.DBConnection;

import javax.swing.*;
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

    @FXML
    private Button ReportButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private TableColumn<Appointments, Integer> apmtContactCol;

    @FXML
    private TableColumn<Appointments, Integer> apmtCustomerIdCol;

    @FXML
    private DatePicker apmtDatePicker;

    @FXML
    private TableColumn<Appointments, String> apmtDescriptionCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> apmtEndDateCol;

    @FXML
    private TableColumn<?, ?> apmtEndTimeCol;

    @FXML
    private TableColumn<Appointments, Integer> apmtIdCol;

    @FXML
    private TableColumn<Appointments, String> apmtLocationCol;

    @FXML
    private TableColumn<Appointments, LocalDateTime> apmtStartDateCol;

    @FXML
    private TableColumn<?, ?> apmtStartTimeCol;

    @FXML
    private TableView<Appointments> apmtTableView;

    @FXML
    private TableColumn<Appointments, String> apmtTitleCol;

    @FXML
    private TableColumn<Appointments, String> apmtTypeCol;

    @FXML
    private TableColumn<Appointments, Integer> apmtUserIdCol;

    @FXML
    private RadioButton byMonthRadio;

    @FXML
    private RadioButton byWeekRadio;

    @FXML
    private Button logoutButton;

    @FXML
    private Button modifyAppointmentButton;

    @FXML
    private Label setTimeLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private RadioButton viewAllRadio;

    private Stage stage;
    private Scene scene;


    @FXML
    void onAddApmt(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "AddAppointment.fxml", "Schedule Appointment");
    }

    @FXML
    void onLogOut(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "login-form.fxml", "Scheduling Application");
    }

    @FXML
    void onModifyApmt(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "UpdateAppointment.fxml", "Update Appointment");
    }

    @FXML
    void onReports(ActionEvent event) {

    }

    @FXML
    void onViewCustomers(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
