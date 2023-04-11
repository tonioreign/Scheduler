package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.Reports;
import sample.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDB {
    /**
     * SQL Query that pulls the exact data needed: Countries and total appointment per country.
     * @throws SQLException
     * @return countriesObservableList
     */
    public static ObservableList<Reports> getCountries() throws SQLException {
        ObservableList<Reports> countriesObservableList = FXCollections.observableArrayList();
        String sql = "select countries.Country, count(*) as countryCount from customers inner join first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID inner join countries on countries.Country_ID = first_level_divisions.Country_ID where  customers.Division_ID = first_level_divisions.Division_ID group by first_level_divisions.Country_ID order by count(*) desc";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            String countryName = rs.getString("Country");
            int countryCount = rs.getInt("countryCount");
            Reports report = new Reports(countryName, countryCount);
            countriesObservableList.add(report);

        }

        return countriesObservableList;
    }
}
