package sample.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.db.AppointmentDB;
import sample.misc.AccessMethod;
import sample.models.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
    private AnchorPane appointmentPanel;

    @FXML
    private RadioButton byCustomerRadio;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
