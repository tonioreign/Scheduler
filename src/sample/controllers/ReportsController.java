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
     * Initialize and setup fields on the form.
     * @throws SQLException
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
     * Fill fxml form with appointment data by contact.
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

            for (Contacts contact: getAllContacts) {
                if (contactName.equals(contact.getContactName())) {
                    contactID = contact.getContactID();
                }
            }

            for (Appointments appointment: getAllAppointmentData) {
                if (appointment.getApmtContactId() == contactID) {
                    contactAppointmentInfo = appointment;
                    appointmentInfo.add(contactAppointmentInfo);
                }
            }
            allAppointmentsTable.setItems(appointmentInfo);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Total number of customer appointments by type and month report.
     * @throws SQLException
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
     * Custom report to display number of appointments in each Country.
     * @throws SQLException
     */
    public void customerByCountry() throws SQLException {
        try {

            ObservableList<Reports> aggregatedCountries = ReportDB.getCountries();
            ObservableList<Reports> countriesToAdd = FXCollections.observableArrayList();

            //IDE converted
            aggregatedCountries.forEach(countriesToAdd::add);

            customerByCountry.setItems(countriesToAdd);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    /**
     * Button to go back to main menu.
     * @throws IOException
     */
    @FXML
    public void backToMainMenu (ActionEvent event) throws IOException {

        AccessMethod.changeScreen(event, "MainMenu.fxml", "Main Menu");
    }

}