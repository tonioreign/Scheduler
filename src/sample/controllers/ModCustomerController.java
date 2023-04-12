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

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

    @FXML
    void onSave(ActionEvent event) throws IOException {

        AccessMethod.changeScreen(event, "ViewCustomers.fxml", "Customers");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            DBConnection.openConnection();
            Customer selectedCustomer = ViewCustomersController.getModifiedCustomer();

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
