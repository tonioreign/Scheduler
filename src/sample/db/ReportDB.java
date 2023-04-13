package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.Reports;
import sample.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This class provides methods for retrieving and managing report data related to countries and their corresponding
 * appointment counts in a database.
 * It allows for retrieving countries and their corresponding total appointment count per country from the database
 * and creating an ObservableList of Reports objects containing country names and appointment counts.
 */
public class ReportDB {
    /**
     * Retrieves countries and their corresponding total appointment count per country from the database.
     *
     * @return countriesObservableList - the ObservableList of Reports objects containing country names and appointment counts
     * @throws SQLException - if there is an error accessing the database
     */
    public static ObservableList<Reports> getCountries() throws SQLException {
        ObservableList<Reports> countriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT countries.Country, COUNT(*) AS countryCount " +
                "FROM customers " +
                "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "INNER JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID " +
                "GROUP BY first_level_divisions.Country_ID " +
                "ORDER BY COUNT(*) DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String countryName = rs.getString("Country");
                int countryCount = rs.getInt("countryCount");
                Reports report = new Reports(countryName, countryCount);
                countriesObservableList.add(report);
            }
        } catch (SQLException e) {
            // Handle any SQL exception that may occur
            e.printStackTrace();
            throw e;
        }
        return countriesObservableList;
    }

}
