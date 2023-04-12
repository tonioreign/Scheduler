package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.db.AppointmentDB;
import sample.db.CountryDB;
import sample.db.CustomerDB;
import sample.db.FirstLevelDivisionDB;
import sample.misc.AccessMethod;
import sample.models.Appointments;
import sample.models.Country;
import sample.models.Customer;
import sample.models.FirstLevelDivision;
import sample.utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewCustomersController implements Initializable {

    @FXML
    private AnchorPane customerPanel;

    @FXML
    private Button menuButton;

    @FXML
    private TableView<Customer> customerTableView;

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

    private static Customer selectedCustomer = null;

    @FXML
    public static Customer getModifiedCustomer(){
        return selectedCustomer;
    }

    @FXML
    void onAddCustomer(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "AddCustomer.fxml", "Add Customer");

    }

    @FXML
    void onDeleteCustomer(ActionEvent event) throws SQLException {
        Connection connection = DBConnection.openConnection();
        ObservableList<Appointments> getAllAppointmentsList = AppointmentDB.getAllAppointments();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected customer and all appointments? ");
        Optional<ButtonType> confirmation = alert.showAndWait();
        if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
            int deleteCustomerID = customerTableView.getSelectionModel().getSelectedItem().getCustomerID();
            AppointmentDB.deleteAppointment(deleteCustomerID, connection);

            String sqlDelete = "DELETE FROM customers WHERE Customer_ID = ?";
            DBConnection.setPreparedStatement(DBConnection.getConnection(), sqlDelete);

            PreparedStatement psDelete = DBConnection.getPreparedStatement();
            int customerFromTable = customerTableView.getSelectionModel().getSelectedItem().getCustomerID();

            //Delete all customer appointments from database.
            for (Appointments appointment: getAllAppointmentsList) {
                int customerFromAppointments = appointment.getApmtCustomerId();
                if (customerFromTable == customerFromAppointments) {
                    String deleteStatementAppointments = "DELETE FROM appointments WHERE Appointment_ID = ?";
                    DBConnection.setPreparedStatement(DBConnection.getConnection(), deleteStatementAppointments);
                }
            }
            psDelete.setInt(1, customerFromTable);
            psDelete.execute();
            ObservableList<Customer> refreshCustomersList = CustomerDB.getAllCustomers(connection);
            customerTableView.setItems(refreshCustomersList);
        }

    }

    @FXML
    void onMenu(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    }

    @FXML
    void onModCustomer(ActionEvent event) throws IOException {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        AccessMethod.changeScreen(event, "ModCustomer.fxml", "Update Customer");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DBConnection.openConnection();


            ObservableList<Country> allCountries = CountryDB.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivision> allFirstLevelDivisions = FirstLevelDivisionDB.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();
            ObservableList<Customer> allCustomersList = CustomerDB.getAllCustomers(connection);

            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerAddCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            customerZipCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            customerDivisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

            //IDE converted to forEach
            allCountries.stream().map(Country::getCountryName).forEach(countryNames::add);

            // lambda #1
            allFirstLevelDivisions.forEach(firstLevelDivision -> firstLevelDivisionAllNames.add(firstLevelDivision.getDivisionName()));

            customerTableView.setItems(allCustomersList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
