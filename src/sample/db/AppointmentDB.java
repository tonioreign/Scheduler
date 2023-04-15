package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.Appointments;
import sample.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This abstract class represents a base class for managing appointments in a database.
 * It provides common methods for performing CRUD (Create, Read, Update, Delete) operations
 * related to appointments in a database.
 *
 * @author Antonio Jenkins
 */
public abstract class AppointmentDB {

    /**
     * Retrieves all appointments from the database.
     * @return ObservableList of Appointments objects containing all appointments
     * @throws SQLException if an error occurs while accessing the database
     */
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

    /**
     * Deletes an appointment from the database.
     * @param appointmentId the ID of the appointment to be deleted
     * @param conn the database connection to be used for the operation
     * @return the number of rows affected by the delete operation
     * @throws SQLException if an error occurs while accessing the database
     */
    public static int deleteAppointment(int appointmentId, Connection conn) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, appointmentId);
            int result = ps.executeUpdate();
            return result;
        }
    }

    /** Checks if an appointment overlaps with another appointment.
     * @param startDateTime the start date and time of the appointment to be checked
     * @param endDateTime the end date and time of the appointment to be checked
     * @param userID the ID of the user associated with the appointment to be checked
     * @return true if the appointment overlaps with another appointment, false otherwise
     */
    public static boolean checkForAppointmentOverlap(LocalDateTime startDateTime, LocalDateTime endDateTime, int userID) {
        String sql = "SELECT * FROM appointments WHERE (Start BETWEEN ? AND ?) OR (End BETWEEN ? AND ?) AND User_ID = ?";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(startDateTime));
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(endDateTime));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(startDateTime));
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(endDateTime));
            ps.setInt(5, userID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
