package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sample.db.CountryDB;
import sample.db.CustomerDB;
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
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddCustomerController implements Initializable {

    /**
     * Anchor pane for the "Add Customer" panel in the UI.
     */
    @FXML
    private AnchorPane addCustomerPanel;

    /**
     * Text field for the customer ID input.
     */
    @FXML
    private TextField customerIDField;

    /**
     * Text field for the customer name input.
     */
    @FXML
    private TextField customerNameField;

    /**
     * Text field for the phone number input.
     */
    @FXML
    private TextField phoneNumberField;

    /**
     * Text field for the address input.
     */
    @FXML
    private TextField addressField;

    /**
     * Text field for the city input.
     */
    @FXML
    private TextField cityField;

    /**
     * Text field for the zip code input.
     */
    @FXML
    private TextField zipField;

    /**
     * Text field for the state input.
     */
    @FXML
    private TextField stateField;

    /**
     * Combo box for selecting a country.
     */
    @FXML
    private ComboBox<String> countryBox;

    /**
     * Combo box for selecting a division ID.
     */
    @FXML
    private ComboBox<String> divisionIDBox;

    /**
     * Button for canceling the customer addition operation.
     */
    @FXML
    private Button cancelButton;

    /**
     * Button for saving the customer information.
     */
    @FXML
    private Button saveButton;

    /**
     * Event handler for the cancel button
     * Back to the main screen of the application
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
     * Handles the save action event, inserts customer data into the database, and navigates to the "Customers" screen.
     * This method first checks if any of the required fields are empty, and if not, it generates a random customer ID,
     * retrieves the first-level division ID, inserts the customer data into the database, and then navigates to the
     * "Customers" screen.
     *
     * @param event The action event that triggers this method.
     */
    @FXML
    void onSave(ActionEvent event) {
        if (!areFieldsEmpty()) {
            try {
                Integer newCustomerID = generateRandomCustomerID();
                int firstLevelDivisionID = getFirstLevelDivisionID();

                insertCustomerData(newCustomerID, firstLevelDivisionID);
                AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
            } catch (IOException | SQLException e) {
                // Handle any errors
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if any of the required fields (customer name, address, zip, phone number, country, and division) are empty.
     *
     * @return true if any of the required fields are empty, otherwise false.
     */
    private boolean areFieldsEmpty() {
        return customerNameField.getText().isEmpty() || addressField.getText().isEmpty() || zipField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || countryBox.getValue().isEmpty() || divisionIDBox.getValue().isEmpty();
    }

    /**
     * Generates a random customer ID between 0 and 99.
     *
     * @return A random integer as the customer ID.
     */
    private Integer generateRandomCustomerID() {
        return (int) (Math.random() * 100);
    }

    /**
     * Retrieve the ID of the selected first-level division.
     * @return The ID of the selected first-level division.
     * @throws SQLException If there's an issue with the database.
     * @throws NoSuchElementException If the selected division is not found.
     */
    private int getFirstLevelDivisionID() throws SQLException, NoSuchElementException {
        Map<String, FirstLevelDivision> divisionMap = FirstLevelDivisionDB.getAllFirstLevelDivisions().stream().collect(Collectors.toMap(FirstLevelDivision::getDivisionName, Function.identity()));
        String selectedDivisionName = divisionIDBox.getSelectionModel().getSelectedItem();
        FirstLevelDivision selectedDivision = divisionMap.get(selectedDivisionName);
        if (selectedDivision == null) {
            throw new NoSuchElementException("Selected division not found in the divisionMap.");
        }
        return selectedDivision.getDivisionID();
    }

    private void insertCustomerData(Integer newCustomerID, int firstLevelDivisionID) throws SQLException {
        String insertStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(insertStatement)) {
            ps.setInt(1, newCustomerID);
            ps.setString(2, customerNameField.getText());
            ps.setString(3, addressField.getText());
            ps.setString(4, zipField.getText());
            ps.setString(5, phoneNumberField.getText());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, "admin");
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, "admin");
            ps.setInt(10, firstLevelDivisionID);
            ps.executeUpdate();
        }
    }

    /**
     * Event handler for handling changes in the selection of the country dropdown in a customer edit form.
     *
     * @param event The ActionEvent that triggered the event.
     * @throws SQLException If there is an error accessing the database.
     */
    public void customerEditCountryDropDown(ActionEvent event) throws SQLException {
        try {
            String selectedCountry = countryBox.getSelectionModel().getSelectedItem();
            ObservableList<FirstLevelDivision> allFirstLevelDivisions = FirstLevelDivisionDB.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionNames = FXCollections.observableArrayList();
            allFirstLevelDivisions.stream().filter(division -> division.getDivisionName().equals(selectedCountry)).map(FirstLevelDivision::getDivisionName).forEach(firstLevelDivisionNames::add);
            divisionIDBox.setItems(firstLevelDivisionNames);
        } catch (NullPointerException e) {
            // Do nothing
        }
    }

    /**
     * Method called during initialization of the controller, typically used to initialize the UI components
     * and populate data. This method is usually invoked automatically by the JavaFX framework.
     *
     * @param url The URL location of the FXML file.
     * @param resourceBundle The ResourceBundle used to localize the UI components.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = DBConnection.openConnection();

        try {
            ObservableList<Country> allCountries = CountryDB.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivision> allFirstLevelDivisions = FirstLevelDivisionDB.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();

            allCountries.stream().map(Country::getCountryName).forEach(countryNames::add);
            countryBox.setItems(countryNames);
            allFirstLevelDivisions.forEach(firstLevelDivision -> firstLevelDivisionAllNames.add(firstLevelDivision.getDivisionName()));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

