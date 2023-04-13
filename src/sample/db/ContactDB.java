package sample.db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.models.Contacts;
import sample.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This class provides methods for managing contacts in a database.
 * It allows for retrieving all contacts from the database and finding the contact ID
 * for a given contact name.
 */
public class ContactDB {

    /**
     * Retrieves all contacts from the database and creates an ObservableList of Contacts.
     *
     * @return contactsObservableList - the ObservableList of Contacts
     * @throws SQLException - if there is an error accessing the database
     */
    public static ObservableList<Contacts> getAllContacts() throws SQLException {
        ObservableList<Contacts> contactsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Contact_ID, Contact_Name, Email from contacts";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                Contacts contact = new Contacts(contactID, contactName, contactEmail);
                contactsObservableList.add(contact);
            }
        }
        return contactsObservableList;
    }


    /**
     * Retrieves the contact ID for a given contact name from the database.
     *
     * @param contactName - the name of the contact
     * @return contactID - the ID of the contact
     * @throws SQLException - if there is an error accessing the database
     */
    public static String findContactID(String contactName) throws SQLException {
        String contactID = null;
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name = ?")) {
            ps.setString(1, contactName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contactID = rs.getString("Contact_ID");
                }
            }
        }
        return contactID;
    }

}
