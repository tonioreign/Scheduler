package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.misc.AccessMethod;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button ReportButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private TableColumn<?, ?> apmtContactCol;

    @FXML
    private TableColumn<?, ?> apmtCustomerIdCol;

    @FXML
    private DatePicker apmtDatePicker;

    @FXML
    private TableColumn<?, ?> apmtDescriptionCol;

    @FXML
    private TableColumn<?, ?> apmtEndDateCol;

    @FXML
    private TableColumn<?, ?> apmtEndTimeCol;

    @FXML
    private TableColumn<?, ?> apmtIdCol;

    @FXML
    private TableColumn<?, ?> apmtLocationCol;

    @FXML
    private TableColumn<?, ?> apmtStartDateCol;

    @FXML
    private TableColumn<?, ?> apmtStartTimeCol;

    @FXML
    private TableView<?> apmtTableView;

    @FXML
    private TableColumn<?, ?> apmtTitleCol;

    @FXML
    private TableColumn<?, ?> apmtTypeCol;

    @FXML
    private TableColumn<?, ?> apmtUserIdCol;

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
        AccessMethod.changeScreen(event, "AddAppointment.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
