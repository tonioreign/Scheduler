package sample.controllers;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.DatePicker;
        import javafx.scene.control.MenuButton;
        import javafx.scene.control.TextField;
        import sample.misc.AccessMethod;

        import java.io.IOException;
        import java.net.URL;
        import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {

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
    private MenuButton startTimeMenu;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private MenuButton endTimeMenu;

    @FXML
    private MenuButton customerIDMenu;

    @FXML
    private MenuButton userIDMenu;

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    }

    @FXML
    void onSave(ActionEvent event) throws IOException {

        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
