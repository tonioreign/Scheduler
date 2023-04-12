package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.Appointments;
import sample.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AppointmentDB {

    public static ObservableList<Appointments> getAllAppointments() throws SQLException {

        ObservableList<Appointments> getAllApmts = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int apmtID = rs.getInt("Appointment_ID");
            String apmtTitle = rs.getString("Title");
            String apmtDesc = rs.getString("Description");
            String apmtLocation = rs.getString("Location");
            String apmtType = rs.getString("Type");
            //LocalDateTime start = convertTimeDateLocal(rs.getTimestamp("Start").toLocalDateTime());
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            //LocalDateTime end = convertTimeDateLocal(rs.getTimestamp("End").toLocalDateTime());
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int custID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointments apmt = new Appointments(apmtID, apmtTitle, apmtDesc, apmtLocation, apmtType, start, end, custID, userID, contactID);
            getAllApmts.add(apmt);
        }
        return getAllApmts;
    }


    public static int deleteAppointment(int cust, Connection conn)throws SQLException{
        String query = "DELETE FROM appointments WHERE Appointment_ID=?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, cust);
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }
}
