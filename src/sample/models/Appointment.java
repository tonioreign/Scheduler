package sample.models;

import java.time.LocalDateTime;

public class Appointment {
    /**THE APPOINTMENTS ID*/
    private int appointmentId;
    /**THE APPOINTMENTS USER ID*/
    private int userId;
    /**THE APPOINTMENTS CUSTOMER ID*/
    private int customerId;
    /**THE APPOINTMENTS TITLE*/
    private String title;
    /**THE APPOINTMENTS DESCRIPTION*/
    private String description;
    /**THE APPOINTMENTS LOCATION*/
    private String location;
    /**THE APPOINTMENTS CONTACT NAME*/
    private String contact;
    /**THE APPOINTMENTS TYPE*/
    private String type;
    /**THE APPOINTMENTS START TIME*/
    private LocalDateTime start;
    /**THE APPOINTMENTS END TIME*/
    private LocalDateTime end;
    /**THE APPOINTMENTS CREATION DATE*/
    private LocalDateTime createDate;
    /**THE APPOINTMENTS CREATOR*/
    private String createdBy;
    /**WHEN APPOINTMENT WAS UPDATED*/
    private LocalDateTime lastUpdate;
    /**WHO UPDATED THE APPOINTMENT*/
    private String lastUpdateBy;

    public Appointment(String title, String  description, String location, String contact, String type){

        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
    }

    /**GETTERS AND SETTERS*/
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
