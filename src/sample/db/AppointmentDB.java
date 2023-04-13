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
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int apmtID = rs.getInt(1); // Use column index instead of column name
                String apmtTitle = rs.getString(2);
                String apmtDesc = rs.getString(3);
                String apmtLocation = rs.getString(4);
                String apmtType = rs.getString(5);
                LocalDateTime start = rs.getTimestamp(6).toLocalDateTime();
                LocalDateTime end = rs.getTimestamp(7).toLocalDateTime();
                int custID = rs.getInt(8);
                int userID = rs.getInt(9);
                int contactID = rs.getInt(10);
                Appointments apmt = new Appointments(apmtID, apmtTitle, apmtDesc, apmtLocation, apmtType, start, end, custID, userID, contactID);
                getAllApmts.add(apmt);
            }
        }
        return getAllApmts;
    }



    public static int deleteAppointment(int appointmentId, Connection conn) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentId);
            int result = ps.executeUpdate();
            return result;
        }
    }
}
