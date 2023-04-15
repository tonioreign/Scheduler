package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.AppointmentDB;
import sample.db.ContactDB;
import sample.db.ReportDB;
import sample.misc.AccessMethod;
import sample.models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class and methods to display 3 reports.
 *
 * @author Antonio Jenkins
 */
public class ReportsController {

    /**
     * TableView for displaying all appointments in the UI.
     */
    @FXML
    private TableView<Appointments> allApmtTable;

    /**
     * TableColumn for displaying appointment contact information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtContact;

    /**
     * TableColumn for displaying appointment customer ID information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtCustomerID;

    /**
     * TableColumn for displaying appointment description information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtDesc;

    /**
     * TableColumn for displaying appointment end date and time information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtEnd;

    /**
     * TableColumn for displaying appointment ID information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtID;

    /**
     * TableColumn for displaying appointment location information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtLocation;

    /**
     * TableColumn for displaying appointment start date and time information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtStart;

    /**
     * TableColumn for displaying appointment title information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtTitle;

    /**
     * TableColumn for displaying appointment type information in the UI.
     */
    @FXML
    private TableColumn<?, ?> appointmentType;

    /**
     * TableColumn for displaying totals by appointment type information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtTotalTypeCol;

    /**
     * TableColumn for displaying totals by month information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtTotalMonth;

    /**
     * TableColumn for displaying month total information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apptTotalsMonthTotal;

    /**
     * TableColumn for displaying type total information in the UI.
     */
    @FXML
    private TableColumn<?, ?> apmtTotalsTypeTotalCol;

    /**
     * ComboBox for selecting contact schedule contact in the UI.
     */
    @FXML
    private ComboBox<String> contactScheduleContactBox;

    /**
     * TableView for displaying appointment type totals in the UI.
     */
    @FXML
    private TableView<ReportType> appointmentTotalsAppointmentType;

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
        apmtID.setCellValueFactory(new PropertyValueFactory<>("apmtId"));
        apmtTitle.setCellValueFactory(new PropertyValueFactory<>("apmtTitle"));
        apmtDesc.setCellValueFactory(new PropertyValueFactory<>("apmtDescription"));
        apmtLocation.setCellValueFactory(new PropertyValueFactory<>("apmtLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("apmtType"));
        apmtStart.setCellValueFactory(new PropertyValueFactory<>("apmtStart"));
        apmtEnd.setCellValueFactory(new PropertyValueFactory<>("apmtEnd"));
        apmtCustomerID.setCellValueFactory(new PropertyValueFactory<>("apmtCustomerId"));
        apmtContact.setCellValueFactory(new PropertyValueFactory<>("apmtContactId"));
        apmtTotalTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        apmtTotalsTypeTotalCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
        apmtTotalMonth.setCellValueFactory(new PropertyValueFactory<>("apmtMonth"));
        apptTotalsMonthTotal.setCellValueFactory(new PropertyValueFactory<>("apmtTotal"));

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
            ObservableList<Appointments> getAllAppointmentData = AppointmentDB.getAllAppointments();
            ObservableList<Contacts> getAllContacts = ContactDB.getAllContacts();

            String contactName = contactScheduleContactBox.getSelectionModel().getSelectedItem();

            if (contactName != null) {
                // Find the contact ID based on the selected contact name
                int contactID = getAllContacts.stream()
                        .filter(contact -> contactName.equals(contact.getContactName()))
                        .mapToInt(Contacts::getContactID)
                        .findFirst()
                        .orElse(0);

                // Filter appointments based on the contact ID
                ObservableList<Appointments> appointmentInfo = getAllAppointmentData.stream()
                        .filter(appointment -> appointment.getApmtContactId() == contactID)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                allApmtTable.setItems(appointmentInfo);
            }
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
            ObservableList<MonthlyReport> reportMonths = FXCollections.observableArrayList();
            ObservableList<ReportType> reportType = FXCollections.observableArrayList();

            // Count appointments by month
            Map<Month, Long> monthCountMap = getAllAppointments.stream()
                    .collect(Collectors.groupingBy(appointment -> appointment.getApmtStart().getMonth(), Collectors.counting()));

            // lambda #2 expression to add the month and count to the reportMonths list
            monthCountMap.forEach((month, count) -> reportMonths.add(new MonthlyReport(month.name(), count.intValue())));

            appointmentTotalAppointmentByMonth.setItems(reportMonths);

            // Count appointments by type
            Map<String, Long> typeCountMap = getAllAppointments.stream()
                    .collect(Collectors.groupingBy(Appointments::getApmtType, Collectors.counting()));

            typeCountMap.forEach((type, count) -> reportType.add(new ReportType(type, count.intValue())));

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
     *
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