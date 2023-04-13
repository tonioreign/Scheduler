package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sample.db.CountryDB;
import sample.db.FirstLevelDivisionDB;
import sample.misc.AccessMethod;
import sample.models.Country;
import sample.models.Customer;
import sample.models.FirstLevelDivision;
import sample.utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ModCustomerController implements Initializable {

    /**
     * AnchorPane for the modify customer panel in the UI.
     */
    @FXML
    private AnchorPane modCustomerPanel;

    /**
     * TextField for entering the customer ID in the UI.
     */
    @FXML
    private TextField customerIDField;

    /**
     * TextField for entering the customer name in the UI.
     */
    @FXML
    private TextField customerNameField;

    /**
     * TextField for entering the phone number in the UI.
     */
    @FXML
    private TextField phoneNumberField;

    /**
     * TextField for entering the address in the UI.
     */
    @FXML
    private TextField addressField;

    /**
     * TextField for entering the city in the UI.
     */
    @FXML
    private TextField cityField;

    /**
     * TextField for entering the ZIP code in the UI.
     */
    @FXML
    private TextField zipField;

    /**
     * TextField for entering the state in the UI.
     */
    @FXML
    private TextField stateField;

    /**
     * ComboBox for selecting the country in the UI.
     */
    @FXML
    private ComboBox<String> countryBox;

    /**
     * ComboBox for selecting the division ID in the UI.
     */
    @FXML
    private ComboBox<String> divisionIDBox;

    /**
     * Button for canceling the modification in the UI.
     */
    @FXML
    private Button cancelButton;

    /**
     * Button for saving the modified customer in the UI.
     */
    @FXML
    private Button saveButton;

    /**
     * Customer object for storing the selected customer.
     */
    private Customer selectedCustomer = null;

    /**
     * Event handler for the cancel button
     * Back to the customer screen of the application
     *
     * @param event The ActionEvent object that triggered the event.
     *              This is typically provided by the JavaFX framework automatically
     *              when the event handler is invoked.
     * @throws IOException If an I/O exception occurs during the screen change process.
     *                     This exception is thrown by the AccessMethod.changeScreen() method,
     *                     which is responsible for changing the screen or scene in the JavaFX application.
     */
    @FXML
    void onCancel(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

    /**
     * Saves the updated customer information to the database when the "Save" button is clicked.
     * This method performs the following steps:
     * 1. Retrieves the updated customer information from the input fields.
     * 2. Performs validation to check if all required fields are not empty.
     * 3. Updates the customer information in the database using an SQL UPDATE statement.
     * 4. Navigates back to the "ViewCustomers.fxml" screen after the update is successful.
     *
     * @param event The ActionEvent triggered by the "Save" button click.
     * @throws IOException if an I/O error occurs during navigation to the next screen.
     */
    @FXML
    void onSave(ActionEvent event) throws IOException {
        try {
            Connection connection = DBConnection.openConnection();

            if (!customerNameField.getText().isEmpty() || !addressField.getText().isEmpty() || !zipField.getText().isEmpty() || !phoneNumberField.getText().isEmpty() || !countryBox.getValue().isEmpty() || !divisionIDBox.getValue().isEmpty())
            {

                int firstLevelDivisionName = 0;
                for (FirstLevelDivision firstLevelDivision : FirstLevelDivisionDB.getAllFirstLevelDivisions()) {
                    if (divisionIDBox.getSelectionModel().getSelectedItem().equals(firstLevelDivision.getDivisionName())) {
                        firstLevelDivisionName = firstLevelDivision.getDivisionID();
                    }
                }
                String insertStatement = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
                DBConnection.setPreparedStatement(DBConnection.getConnection(), insertStatement);
                PreparedStatement ps = DBConnection.getPreparedStatement();
                ps.setInt(1, Integer.parseInt(customerIDField.getText()));
                ps.setString(2, customerNameField.getText());
                ps.setString(3, addressField.getText());
                ps.setString(4, zipField.getText());
                ps.setString(5, phoneNumberField.getText());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(7, "admin");
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setInt(10, firstLevelDivisionName);
                ps.setInt(11, Integer.parseInt(customerIDField.getText()));
                ps.executeUpdate();

            }
            AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when the country drop-down box is edited.
     *
     * @param event The action event triggered by the country drop-down box.
     * @throws SQLException If an SQL exception occurs during database operations.
     */
    public void customerEditCountryDropDown(ActionEvent event) throws SQLException {
        try {
            DBConnection.openConnection();

            String selectedCountry = countryBox.getSelectionModel().getSelectedItem();

            ObservableList<FirstLevelDivision> getAllFirstLevelDivisions = FirstLevelDivisionDB.getAllFirstLevelDivisions();

            ObservableList<String> flDivisionUS = FXCollections.observableArrayList();
            ObservableList<String> flDivisionUK = FXCollections.observableArrayList();
            ObservableList<String> flDivisionCanada = FXCollections.observableArrayList();

            getAllFirstLevelDivisions.forEach(firstLevelDivision -> {
                if (firstLevelDivision.getCountry_ID() == 1) {
                    flDivisionUS.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountry_ID() == 2) {
                    flDivisionUK.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountry_ID() == 3) {
                    flDivisionCanada.add(firstLevelDivision.getDivisionName());
                }
            });

            //needs a little revision
            if (selectedCountry.equals("U.S")) {
                divisionIDBox.setItems(flDivisionUS);
            }
            else if (selectedCountry.equals("UK")) {
                divisionIDBox.setItems(flDivisionUK);
            }
            else if (selectedCountry.equals("Canada")) {
                divisionIDBox.setItems(flDivisionCanada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class for the corresponding view.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resource bundle used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            DBConnection.openConnection();
            Connection connection = DBConnection.getConnection();
            selectedCustomer = ViewCustomersController.getModifiedCustomer();
            ObservableList<Country> allCountries = CountryDB.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivision> allFirstLevelDivisions = FirstLevelDivisionDB.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();

            allCountries.stream().map(Country::getCountryName).forEach(countryNames::add);
            countryBox.setItems(countryNames);
            allFirstLevelDivisions.stream().map(FirstLevelDivision::getDivisionName).forEach(countryNames::add);
            divisionIDBox.setItems(countryNames);

            allFirstLevelDivisions.forEach(firstLevelDivision -> firstLevelDivisionAllNames.add(firstLevelDivision.getDivisionName()));
            String divisionName = "", countryName = "";

            if (selectedCustomer != null) {
                ObservableList<Country> getAllCountries = CountryDB.getCountries();
                ObservableList<FirstLevelDivision> getFLDivisionNames = FirstLevelDivisionDB.getAllFirstLevelDivisions();
                ObservableList<String> allFLDivision = FXCollections.observableArrayList();

                divisionIDBox.setItems(allFLDivision);

                customerIDField.setText(String.valueOf((selectedCustomer.getCustomerID())));
                customerNameField.setText(selectedCustomer.getCustomerName());
                addressField.setText(selectedCustomer.getCustomerAddress());
                zipField.setText(selectedCustomer.getCustomerPostalCode());
                phoneNumberField.setText(selectedCustomer.getCustomerPhone());
                countryBox.setValue(selectedCustomer.getDivisionName());


                for (FirstLevelDivision flDivision: getFLDivisionNames) {
                    allFLDivision.add(flDivision.getDivisionName());
                    int countryIDToSet = flDivision.getCountry_ID();

                    if (flDivision.getDivisionID() == selectedCustomer.getCustomerDivisionID()) {
                        divisionName = flDivision.getDivisionName();

                        for (Country country: getAllCountries) {
                            if (country.getCountryID() == countryIDToSet) {
                                countryName = country.getCountryName();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
