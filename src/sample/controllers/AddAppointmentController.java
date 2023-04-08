package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    @FXML
    private TextField titleField;

    @FXML
    private TextField descField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField typeField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private SplitMenuButton startTimeMenu;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private SplitMenuButton endTimeMenu;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField userIdField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
