package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.Country;
import sample.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This class provides methods for retrieving and managing countries in a database.
 * It allows for retrieving a list of countries from the database.
 */
public class CountryDB {

    /**
     * Retrieves the list of countries from the database and creates an ObservableList of Country objects.
     *
     * @return countriesObservableList - the ObservableList of Country objects
     * @throws SQLException - if there is an error accessing the database
     */
    public static ObservableList<Country> getCountries() throws SQLException {
        ObservableList<Country> countriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Country_ID, Country from countries";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country country = new Country(countryID, countryName);
                countriesObservableList.add(country);
            }
        }
        return countriesObservableList;
    }

}
