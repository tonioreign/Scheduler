package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.db.AppointmentDB;
import sample.db.ContactDB;
import sample.db.ReportDB;
import sample.misc.AccessMethod;
import sample.models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class and methods to display 3 reports.
 */

public class ReportsController {

    /**
     * TableView for displaying all appointments in the UI.
     */
    @FXML
    private TableView<Appointments> allAppointmentsTable;

    /**
     * TableColumn for displaying appointment contact information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentContact;

    /**
     * TableColumn for displaying appointment customer ID information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentCustomerID;

    /**
     * TableColumn for displaying appointment description information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentDescription;

    /**
     * TableColumn for displaying appointment end date and time information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentEnd;

    /**
     * TableColumn for displaying appointment ID information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentID;

    /**
     * TableColumn for displaying appointment location information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentLocation;

    /**
     * TableColumn for displaying appointment start date and time information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentStart;

    /**
     * TableColumn for displaying appointment title information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentTitle;

    /**
     * TableColumn for displaying appointment type information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentType;

    /**
     * TableColumn for displaying totals by appointment type information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentTotalsAppointmentTypeCol;

    /**
     * TableColumn for displaying totals by month information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentTotalsByMonth;

    /**
     * TableColumn for displaying month total information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentTotalsMonthTotal;

    /**
     * TableColumn for displaying type total information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentTotalsTypeTotalCol;

    /**
     * Button for navigating back to the main menu in the UI.
     */
    @FXML
    private Button backToMainMenu;

    /**
     * ComboBox for selecting contact schedule contact in the UI.
     */
    @FXML
    private ComboBox<String> contactScheduleContactBox;

    /**
     * TableColumn for displaying contact ID information in the UI.
     */
    @FXML
    private TableColumn<?, ?> tableContactID;

    /**
     * TableView for displaying appointment type totals in the UI.
     */
    @FXML
    private TableView<ReportType> appointmentTotalsAppointmentType;

    /**
     * Tab for displaying appointment totals information in the UI.
     */
    @FXML
    private Tab appointmentTotalsTab;

    /**
     * TableView for displaying monthly appointment totals in the UI.
     */
    @FXML
    private TableView<MonthlyReport> appointmentTotalAppointmentByMonth;

    /**
     * TableView for displaying customer by country report in the UI.
     */
    @FXML
    private TableView<Reports> customerByCountry;

    /**
     * TableColumn for displaying country name information in the UI.
     */
    @FXML
    private TableColumn<?, ?> countryName;

    /**
     * TableColumn for displaying country counter information in the UI.
     */
    @FXML
    private TableColumn<?, ?> countryCounter;

    /**
     * Initializes the appointment scheduler view with data from the database.
     * This method sets up the cell value factories for the appointment scheduler table columns,
     * and populates the contact selection box with contact names from the database.
     * @throws SQLException If an SQL exception occurs while retrieving data from the database.
     */
    public void initialize() throws SQLException {

        countryName.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        countryCounter.setCellValueFactory(new PropertyValueFactory<>("countryCount"));
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("apmtId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("apmtTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("apmtDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("apmtLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("apmtType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("apmtStart"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("apmtEnd"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("apmtCustomerId"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("apmtContactId"));
        appointmentTotalsAppointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentTotalsTypeTotalCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
        appointmentTotalsByMonth.setCellValueFactory(new PropertyValueFactory<>("apmtMonth"));
        appointmentTotalsMonthTotal.setCellValueFactory(new PropertyValueFactory<>("apmtTotal"));

        ObservableList<Contacts> contactsObservableList = ContactDB.getAllContacts();
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();
        contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));
        contactScheduleContactBox.setItems(allContactsNames);

    }

    /**
     * Retrieves and displays appointment data for a specific contact in the appointment scheduler view.
     * This method retrieves all appointment data and contact data from the database,
     * matches the selected contact name with the corresponding contact ID,
     * and displays the appointments associated with that contact in the appointments table.
     * @throws SQLException If an SQL exception occurs while retrieving data from the database.
     */
    @FXML
    public void appointmentDataByContact() {
        try {
            int contactID = 0;
            ObservableList<Appointments> getAllAppointmentData = AppointmentDB.getAllAppointments();
            ObservableList<Appointments> appointmentInfo = FXCollections.observableArrayList();
            ObservableList<Contacts> getAllContacts = ContactDB.getAllContacts();

            Appointments contactAppointmentInfo;

            String contactName = contactScheduleContactBox.getSelectionModel().getSelectedItem();

            if (contactName != null) {
                for (Contacts contact: getAllContacts) {
                    if (contactName.equals(contact.getContactName())) {
                        contactID = contact.getContactID();
                        break; // exit loop once contact ID is found
                    }
                }

                for (Appointments appointment: getAllAppointmentData) {
                    if (appointment.getApmtContactId() == contactID) {
                        contactAppointmentInfo = appointment;
                        appointmentInfo.add(contactAppointmentInfo);
                    }
                }
            }
            allAppointmentsTable.setItems(appointmentInfo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Calculates and displays appointment totals by month and appointment type in the appointment totals tab.
     * This method retrieves all appointment data from the database,
     * calculates the total number of appointments for each month and appointment type,
     * and displays the results in corresponding tables in the appointment totals tab.
     * @throws SQLException If an SQL exception occurs while retrieving data from the database.
     */
    public void appointmentTotalsTab() throws SQLException {
        try {
            ObservableList<Appointments> getAllAppointments = AppointmentDB.getAllAppointments();
            ObservableList<Month> appointmentMonths = FXCollections.observableArrayList();
            ObservableList<Month> monthOfAppointments = FXCollections.observableArrayList();

            ObservableList<String> appointmentType = FXCollections.observableArrayList();
            ObservableList<String> uniqueAppointment = FXCollections.observableArrayList();

            ObservableList<ReportType> reportType = FXCollections.observableArrayList();
            ObservableList<MonthlyReport> reportMonths = FXCollections.observableArrayList();


            //IDE converted to Lambda
            getAllAppointments.forEach(appointments -> {
                appointmentType.add(appointments.getApmtType());
            });

            //IDE converted to Lambda
            getAllAppointments.stream().map(appointment -> {
                return appointment.getApmtStart().getMonth();
            }).forEach(appointmentMonths::add);

            //IDE converted to Lambda
            appointmentMonths.stream().filter(month -> {
                return !monthOfAppointments.contains(month);
            }).forEach(monthOfAppointments::add);

            for (Appointments appointments: getAllAppointments) {
                String appointmentsAppointmentType = appointments.getApmtType();
                if (!uniqueAppointment.contains(appointmentsAppointmentType)) {
                    uniqueAppointment.add(appointmentsAppointmentType);
                }
            }

            for (Month month: monthOfAppointments) {
                int totalMonth = Collections.frequency(appointmentMonths, month);
                String monthName = month.name();
                MonthlyReport appointmentMonth = new MonthlyReport(monthName, totalMonth);
                reportMonths.add(appointmentMonth);
            }
            appointmentTotalAppointmentByMonth.setItems(reportMonths);

            for (String type: uniqueAppointment) {
                String typeToSet = type;
                int typeTotal = Collections.frequency(appointmentType, type);
                ReportType appointmentTypes = new ReportType(typeToSet, typeTotal);
                reportType.add(appointmentTypes);
            }
            appointmentTotalsAppointmentType.setItems(reportType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves and displays customer data aggregated by country in the customer by country tab.
     * This method retrieves customer data from the database,
     * aggregates the data by country using the {@link ReportDB#getCountries()} method,
     * and displays the aggregated customer data in the appropriate table in the customer by country tab.
     * @throws SQLException If an SQL exception occurs while retrieving data from the database.
     */
    public void customerByCountry() throws SQLException {
        try {
            ObservableList<Reports> aggregatedCountries = ReportDB.getCountries();
            customerByCountry.setItems(aggregatedCountries);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the event when the "Back to Main Menu" button is clicked.
     * This method changes the screen to the "MainMenu.fxml" view using the {@link AccessMethod#changeScreen(ActionEvent, String, String)} method,
     * with the event, the FXML file name, and the window title as parameters.
     * @param event The ActionEvent triggered by the "Back to Main Menu" button.
     * @throws IOException If an I/O exception occurs while loading the FXML file.
     */
    @FXML
    public void backToMainMenu (ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to go back to the main menu?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
        }
    }
}