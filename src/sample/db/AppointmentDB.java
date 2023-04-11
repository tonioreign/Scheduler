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
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            String appointmentType = rs.getString("Type");
            //LocalDateTime start = convertTimeDateLocal(rs.getTimestamp("Start").toLocalDateTime());
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            //LocalDateTime end = convertTimeDateLocal(rs.getTimestamp("End").toLocalDateTime());
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, start, end, customerID, userID, contactID);
            getAllApmts.add(appointment);
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
