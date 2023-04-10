package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import sample.misc.AccessMethod;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewCustomersController implements Initializable {

    @FXML
    private AnchorPane customerPanel;

    @FXML
    private Button menuButton;

    @FXML
    private TableView<?> customerTableView;

    @FXML
    private TableColumn<?, ?> customerIDCol;

    @FXML
    private TableColumn<?, ?> customerNameCol;

    @FXML
    private TableColumn<?, ?> customerAddCol;

    @FXML
    private TableColumn<?, ?> customerZipCol;

    @FXML
    private TableColumn<?, ?> customerPhoneCol;

    @FXML
    private TableColumn<?, ?> customerCreationDateCol;

    @FXML
    private TableColumn<?, ?> customerCreatedByCol;

    @FXML
    private TableColumn<?, ?> customerLastUpdatedCol;

    @FXML
    private TableColumn<?, ?> customerLastUpdatedByCol;

    @FXML
    private TableColumn<?, ?> customerDivisionIDCol;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button modCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    void onAddCustomer(ActionEvent event) {
        System.out.println("Opening add customer");

    }

    @FXML
    void onDeleteCustomer(ActionEvent event) {
        System.out.println("Deleting customer");
    }

    @FXML
    void onMenu(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "MainMenu.fxml");
    }

    @FXML
    void onModCustomer(ActionEvent event) {
        System.out.println("Opening mod customer");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
