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

    @FXML
    private AnchorPane modCustomerPanel;

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
    private ComboBox<String> countryBox;

    @FXML
    private ComboBox<String> divisionIDBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    private Customer selectedCustomer = null;

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

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
