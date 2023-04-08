package sample.models;

import java.time.LocalDateTime;

public class Appointments {
    private int apmtId;
    private String apmtTitle;
    private String apmtDescription;
    private String apmtLocation;
    private String apmtType;
    private LocalDateTime apmtStart;
    //private String start;
    private LocalDateTime apmtEnd;
    public int apmtCustomerId;
    public int apmtUserId;
    public int apmtContactId;

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
