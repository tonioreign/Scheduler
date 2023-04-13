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
import java.util.ResourceBundle;

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

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

    @FXML
    void onSave(ActionEvent event) throws IOException, SQLException {

        if (!customerNameField.getText().isEmpty() || !addressField.getText().isEmpty() || !zipField.getText().isEmpty() || !phoneNumberField.getText().isEmpty() || !countryBox.getValue().isEmpty() || !divisionIDBox.getValue().isEmpty()) {
            // Generate random ID for new customer
            Integer newCustomerID = (int) (Math.random() * 100);

            // Get first level division ID
            int firstLevelDivisionID = 0;
            Map<String, FirstLevelDivision> divisionMap = new HashMap<>();
            for (FirstLevelDivision firstLevelDivision : FirstLevelDivisionDB.getAllFirstLevelDivisions()) {
                divisionMap.put(firstLevelDivision.getDivisionName(), firstLevelDivision);
            }
            // Retrieve the FirstLevelDivision object using selected division name
            String selectedDivisionName = divisionIDBox.getSelectionModel().getSelectedItem();
            if (divisionMap.containsKey(selectedDivisionName)) {
                FirstLevelDivision selectedDivision = divisionMap.get(selectedDivisionName);
                firstLevelDivisionID = selectedDivision.getDivisionID();
            }

            // Insert data into database
            String insertStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
            try (Connection conn = DBConnection.getConnection();
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

                AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
            } catch (SQLException e) {
                // Handle any database errors
                e.printStackTrace();
                // Add appropriate error handling or logging as needed
            }
        }
    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = DBConnection.getConnection();

        try {
            ObservableList<Country> allCountries = CountryDB.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivision> allFirstLevelDivisions = FirstLevelDivisionDB.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();

            allCountries.stream().map(Country::getCountryName).forEach(countryNames::add);
            countryBox.setItems(countryNames);
            allFirstLevelDivisions.stream().map(FirstLevelDivision::getDivisionName).forEach(countryNames::add);
            divisionIDBox.setItems(countryNames);

            allFirstLevelDivisions.forEach(firstLevelDivision -> firstLevelDivisionAllNames.add(firstLevelDivision.getDivisionName()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

