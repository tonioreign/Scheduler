package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.Country;
import sample.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDB {

    /**
     * ObservableList that queries Country_ID and Country from the countries database table.
     * @throws SQLException
     * @return countriesObservableList
     */
    public static ObservableList<Country> getCountries() throws SQLException {
        ObservableList<Country> countriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Country_ID, Country from countries";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            Country country = new Country(countryID, countryName);
            countriesObservableList.add(country);
        }
        return countriesObservableList;
    }
}
