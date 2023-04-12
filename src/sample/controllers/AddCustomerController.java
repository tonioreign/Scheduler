package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.misc.AccessMethod;
import sample.utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private AnchorPane addCustomerPanel;

    @FXML
    private TextField customerIDField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField zipField;

    @FXML
    private TextField stateField;

    @FXML
    private MenuButton countryMenu;

    @FXML
    private MenuButton divisionIDMenu;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

    @FXML
    void onSave(ActionEvent event) throws IOException, SQLException {

        //create random ID for new customer id
        Integer newCustomerID = (int) (Math.random() * 100);

        String insertStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        DBConnection.setPreparedStatement(DBConnection.getConnection(), insertStatement);
        PreparedStatement ps = DBConnection.getPreparedStatement();
        ps.setInt(1, newCustomerID);
        ps.setString(2, customerNameField.getText());
        ps.setString(3, addressField.getText());
        ps.setString(4, zipField.getText());
        ps.setString(5, phoneNumberField.getText());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, "admin");
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "admin");
       // ps.setInt(10, firstLevelDivisionName);
        ps.execute();
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
