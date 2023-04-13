package sample.models;

public class ReportType {
    /**
     * The type of appointment.
     */
    private String appointmentType;

    /**
     * The total number of appointments of the specified type.
     */
    private int appointmentTotal;

    /**
     * Constructs a new ReportType object with the given appointment type and appointment total.
     *
     * @param appointmentType The type of appointment.
     * @param appointmentTotal The total count of appointments of this type.
     */
    public ReportType(String appointmentType, int appointmentTotal) {
        this.appointmentType = appointmentType;
        this.appointmentTotal = appointmentTotal;
    }

    /**
     * getters
     */
    public String getAppointmentType() {

        return appointmentType;
    }

    public int getAppointmentTotal() {

        return appointmentTotal;
    }
}
