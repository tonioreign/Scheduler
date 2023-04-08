package sample.models;

public class Reports {
    private int countryCount;
    public int appointmentTotal;
    public String appointmentMonth;
    private String countryName;


    /**
     * @param countryCount
     * @param countryName
     */
    public Reports(int countryCount, String countryName) {
        this.countryCount = countryCount;
        this.countryName = countryName;

    }

    /**GETTERS AND SETTERS*/
    public int getCountryCount() {
        return countryCount;
    }

    public void setCountryCount(int countryCount) {
        this.countryCount = countryCount;
    }

    public int getAppointmentTotal() {
        return appointmentTotal;
    }

    public void setAppointmentTotal(int appointmentTotal) {
        this.appointmentTotal = appointmentTotal;
    }

    public String getAppointmentMonth() {
        return appointmentMonth;
    }

    public void setAppointmentMonth(String appointmentMonth) {
        this.appointmentMonth = appointmentMonth;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
