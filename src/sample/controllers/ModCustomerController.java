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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
/** This class is the controller for the modify customer screen.
 *  It handles the logic for the modify customer screen.
 *
 *  @author Antonio Jenkins
 * */
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Any unsaved changes will be lost.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
        }
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
                int firstLevelDivisionID = getFirstLevelDivisionID();

                insertCustomerData(firstLevelDivisionID);
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

    /** Inserts the customer data into the database.
     * @param firstLevelDivisionID The ID of the first-level division.
     * @throws SQLException If there's an issue with the database.
     */
    private void insertCustomerData(int firstLevelDivisionID) throws SQLException {
        String insertStatement = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        try (Connection conn = DBConnection.openConnection();
             PreparedStatement ps = conn.prepareStatement(insertStatement)) {
            ps.setInt(1, Integer.parseInt(customerIDField.getText()));
            ps.setString(2, customerNameField.getText());
            ps.setString(3, addressField.getText());
            ps.setString(4, zipField.getText());
            ps.setString(5, phoneNumberField.getText());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, "admin");
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, "admin");
            ps.setInt(10, firstLevelDivisionID);
            ps.setInt(11, Integer.parseInt(customerIDField.getText()));
            ps.executeUpdate();
        }
    }

    /**
     * Handles the event when the country drop-down box is edited.
     * #lambda 3 - stream and filter first level divisions by country ID and add to observable list for each country ID
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

            // lambda 3 - stream and filter first level divisions by country ID and add to observable list for each country ID
            getAllFirstLevelDivisions.forEach(firstLevelDivision -> {
                if (firstLevelDivision.getCountry_ID() == 1) {
                    flDivisionUS.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountry_ID() == 2) {
                    flDivisionUK.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountry_ID() == 3) {
                    flDivisionCanada.add(firstLevelDivision.getDivisionName());
                }
            });

            if (selectedCountry.equals("U.S"))
                divisionIDBox.setItems(flDivisionUS);
            else if (selectedCountry.equals("Canada"))
                divisionIDBox.setItems(flDivisionUK);
            else if (selectedCountry.equals("UK"))
                divisionIDBox.setItems(flDivisionCanada);

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
            Customer selectedCustomer = ViewCustomersController.getModifiedCustomer();
            ObservableList<Country> allCountries = CountryDB.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivision> allFirstLevelDivisions = FirstLevelDivisionDB.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();

            allCountries.stream().map(Country::getCountryName).forEach(countryNames::add);
            countryBox.setItems(countryNames);
            allFirstLevelDivisions.forEach(firstLevelDivision -> firstLevelDivisionAllNames.add(firstLevelDivision.getDivisionName()));

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
                divisionIDBox.setValue(divisionName);
                countryBox.setValue(countryName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
