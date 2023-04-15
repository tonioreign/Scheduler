package sample.models;

import java.time.LocalDateTime;
/**
 * The Appointments class represents an appointment with various properties such as ID, title,
 * description, location, type, start and end times, customer ID, user ID, and contact ID.
 * It provides getters and setters for accessing and modifying the properties.
 *
 * @author Antonio Jenkins
 */
public class Appointments {
    /**
     * The appointment ID.
     */
    private int apmtId;

    /**
     * The appointment title.
     */
    private String apmtTitle;

    /**
     * The appointment description.
     */
    private String apmtDescription;

    /**
     * The appointment location.
     */
    private String apmtLocation;

    /**
     * The appointment type.
     */
    private String apmtType;

    /**
     * The start date and time of the appointment.
     */
    private LocalDateTime apmtStart;

    /**
     * The end date and time of the appointment.
     */
    private LocalDateTime apmtEnd;

    /**
     * The customer ID associated with the appointment.
     * This variable is public, use with caution.
     */
    public int apmtCustomerId;

    /**
     * The user ID associated with the appointment.
     * This variable is public, use with caution.
     */
    public int apmtUserId;

    /**
     * The contact ID associated with the appointment.
     * This variable is public, use with caution.
     */
    public int apmtContactId;


    /**
     * Constructs a new Appointments object with the given parameters.
     *
     * @param apmtId          The appointment ID.
     * @param apmtTitle       The appointment title.
     * @param apmtDescription The appointment description.
     * @param apmtLocation    The appointment location.
     * @param apmtType        The appointment type.
     * @param apmtStart       The start date and time of the appointment.
     * @param apmtEnd         The end date and time of the appointment.
     * @param apmtCustomerId  The customer ID associated with the appointment.
     * @param apmtUserId      The user ID associated with the appointment.
     * @param apmtContactId   The contact ID associated with the appointment.
     */
    public Appointments(int apmtId, String apmtTitle, String apmtDescription,
                        String apmtLocation, String apmtType, LocalDateTime apmtStart, LocalDateTime apmtEnd, int apmtCustomerId,
                        int apmtUserId, int apmtContactId) {
        this.apmtId = apmtId;
        this.apmtTitle = apmtTitle;
        this.apmtDescription = apmtDescription;
        this.apmtLocation = apmtLocation;
        this.apmtType = apmtType;
        this.apmtStart = apmtStart;
        this.apmtEnd = apmtEnd;
        this.apmtCustomerId = apmtCustomerId;
        this.apmtUserId = apmtUserId;
        this.apmtContactId = apmtContactId;
    }


    /**GETTERS AND SETTERS*/
    public int getApmtId() {
        return apmtId;
    }

    public void setApmtId(int apmtId) {
        this.apmtId = apmtId;
    }

    public String getApmtTitle() {
        return apmtTitle;
    }

    public void setApmtTitle(String apmtTitle) {
        this.apmtTitle = apmtTitle;
    }

    public String getApmtDescription() {
        return apmtDescription;
    }

    public void setApmtDescription(String apmtDescription) {
        this.apmtDescription = apmtDescription;
    }

    public String getApmtLocation() {
        return apmtLocation;
    }

    public void setApmtLocation(String apmtLocation) {
        this.apmtLocation = apmtLocation;
    }

    public String getApmtType() {
        return apmtType;
    }

    public void setApmtType(String apmtType) {
        this.apmtType = apmtType;
    }

    public LocalDateTime getApmtStart() {
        return apmtStart;
    }

    public void setApmtStart(LocalDateTime apmtStart) {
        this.apmtStart = apmtStart;
    }

    public LocalDateTime getApmtEnd() {
        return apmtEnd;
    }

    public void setApmtEnd(LocalDateTime apmtEnd) {
        this.apmtEnd = apmtEnd;
    }

    public int getApmtCustomerId() {
        return apmtCustomerId;
    }

    public void setApmtCustomerId(int apmtCustomerId) {
        this.apmtCustomerId = apmtCustomerId;
    }

    public int getApmtUserId() {
        return apmtUserId;
    }

    public void setApmtUserId(int apmtUserId) {
        this.apmtUserId = apmtUserId;
    }

    public int getApmtContactId() {
        return apmtContactId;
    }

    public void setApmtContactId(int apmtContactId) {
        this.apmtContactId = apmtContactId;
    }
}
