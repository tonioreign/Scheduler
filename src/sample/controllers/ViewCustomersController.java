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

    /**
     * AnchorPane for customer panel in the UI.
     */
    @FXML
    private AnchorPane customerPanel;

    /**
     * Button for menu navigation in the UI.
     */
    @FXML
    private Button menuButton;

    /**
     * TableView for displaying customer data in the UI.
     */
    @FXML
    private TableView<Customer> customerTableView;

    /**
     * TableColumn for displaying customer ID in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerIDCol;

    /**
     * TableColumn for displaying customer name in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerNameCol;

    /**
     * TableColumn for displaying customer address in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerAddCol;

    /**
     * TableColumn for displaying customer zip code in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerZipCol;

    /**
     * TableColumn for displaying customer phone number in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerPhoneCol;

    /**
     * TableColumn for displaying customer creation date in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerCreationDateCol;

    /**
     * TableColumn for displaying customer created by user in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerCreatedByCol;

    /**
     * TableColumn for displaying customer last updated date in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerLastUpdatedCol;

    /**
     * TableColumn for displaying customer last updated by user in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerLastUpdatedByCol;

    /**
     * TableColumn for displaying customer division ID in the UI.
     */
    @FXML
    private TableColumn<?, ?> customerDivisionIDCol;

    /**
     * Button for adding a new customer in the UI.
     */
    @FXML
    private Button addCustomerButton;

    /**
     * Button for modifying an existing customer in the UI.
     */
    @FXML
    private Button modCustomerButton;

    /**
     * Button for deleting a customer in the UI.
     */
    @FXML
    private Button deleteCustomerButton;

    /**
     * Selected customer object for storing currently selected customer in the UI.
     */
    private static Customer selectedCustomer = null;

    /**
     * Returns the Customer object that was modified or selected in the UI.
     *
     * @return The Customer object that was modified or selected in the UI, or null if no customer was modified or selected.
     */
    @FXML
    public static Customer getModifiedCustomer(){
        return selectedCustomer;
    }

    /**
     * Handles the "Add Customer" button action event.
     * Changes the screen to the "AddCustomer.fxml" view.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException if an I/O error occurs while loading the "AddCustomer.fxml" view.
     */
    @FXML
    void onAddCustomer(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "AddCustomer.fxml", "Add Customer");

    }

    /**
     * Handles the "Delete Customer" button action event.
     * Deletes the selected customer and all their appointments from the database.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws SQLException if a database error occurs while deleting the customer or appointments.
     */
    @FXML
    void onDeleteCustomer(ActionEvent event) throws SQLException {
        try (Connection connection = DBConnection.openConnection();
             PreparedStatement psDelete = connection.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
             PreparedStatement psDeleteAppointments = connection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?")) {

            ObservableList<Appointments> getAllAppointmentsList = AppointmentDB.getAllAppointments();
            ObservableList<Customer> refreshCustomersList;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected customer and all appointments? ");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                int deleteCustomerID = customerTableView.getSelectionModel().getSelectedItem().getCustomerID();

                // Delete appointments for the customer
                for (Appointments appointment : getAllAppointmentsList) {
                    if (deleteCustomerID == appointment.getApmtCustomerId()) {
                        psDeleteAppointments.setInt(1, appointment.getApmtId());
                        psDeleteAppointments.addBatch();
                    }
                }
                psDeleteAppointments.executeBatch();

                // Delete customer
                psDelete.setInt(1, deleteCustomerID);
                psDelete.execute();

                // Refresh customers list
                refreshCustomersList = CustomerDB.getAllCustomers(connection);
                customerTableView.setItems(refreshCustomersList);
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
            // Display error message to user
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting customer");
            errorAlert.showAndWait();
        }
    }

    /**
     * Handles the "Menu" button action event.
     * Navigates to the main menu screen.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException if an I/O error occurs while changing the screen.
     */
    @FXML
    void onMenu(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    }

    /**
     * Handles the "Modify Customer" button action event.
     * Retrieves the selected customer from the customer table view.
     * Navigates to the customer modification screen.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException if an I/O error occurs while changing the screen.
     */
    @FXML
    void onModCustomer(ActionEvent event) throws IOException {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        AccessMethod.changeScreen(event, "ModCustomer.fxml", "Update Customer");
    }

    /**
     * Initializes the controller for the "Add Customer" screen.
     * Retrieves data from the database to populate the customer table view and other UI components.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resource bundle used to localize the root object, or null if the root object was not localized.
     */
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
