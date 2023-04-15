package sample.models;
/**
 * This class is used to create a report for the number of appointments by type and month.
 * It also creates a report for the number of appointments by location.
 * It also creates a report for the number of customers by country.
 *
 * @author Antonio Jenkins
 */
public class Reports {
    /**
     * The count of countries.
     */
    private int countryCount;

    /**
     * The name of the country.
     */
    private String countryName;

    /**
     * The month for which the appointment report is generated.
     */
    public String appointmentMonth;

    /**
     * The total number of appointments for the month.
     */
    public int appointmentTotal;

    /**
     * Constructs a new Reports object with the given country name and country count.
     *
     * @param countryName The name of the country.
     * @param countryCount The count of the country.
     */
    public Reports(String countryName, int countryCount) {
        this.countryCount = countryCount;
        this.countryName = countryName;
    }

    /**
     * Returns country name for custom report.
     *
     * @return countryName
     */
    public String getCountryName() {

        return countryName;
    }

    /**
     * Total for each country.
     *
     * @return countryCount
     */
    public int getCountryCount() {

        return countryCount;
    }
}
