package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.FirstLevelDivision;
import sample.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This class provides methods for retrieving and managing first level division data in a database.
 * It allows for retrieving all data from the first_level_divisions table and creating an ObservableList of FirstLevelDivision objects.
 */
public class FirstLevelDivisionDB {

    /**
     * Retrieves all data from the first_level_divisions table and creates an ObservableList of FirstLevelDivision objects.
     *
     * @return firstLevelDivisionsObservableList - the ObservableList of FirstLevelDivision objects
     * @throws SQLException - if there is an error accessing the database
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<FirstLevelDivision> firstLevelDivisionsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Division_ID, Division, COUNTRY_ID FROM first_level_divisions";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("COUNTRY_ID");
                FirstLevelDivision firstLevel = new FirstLevelDivision(divisionID, divisionName, countryID);
                firstLevelDivisionsObservableList.add(firstLevel);
            }
        }
        return firstLevelDivisionsObservableList;
    }
}
