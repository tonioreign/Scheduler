package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.FirstLevelDivision;
import sample.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDB {

    /**
     * ObservableList that takes entire first_level_divisions table.
     * @throws SQLException
     * @return firstLevelDivisionsObservableList
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException, SQLException {
        ObservableList<FirstLevelDivision> firstLevelDivisionsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from first_level_divisions";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int country_ID = rs.getInt("COUNTRY_ID");
            FirstLevelDivision firstLevel = new FirstLevelDivision(divisionID, divisionName, country_ID);
            firstLevelDivisionsObservableList.add(firstLevel);
        }
        return firstLevelDivisionsObservableList;
    }
}
