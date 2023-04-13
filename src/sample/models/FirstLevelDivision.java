package sample.models;

public class FirstLevelDivision {
    /**
     * The ID of the division.
     */
    private int divisionID;

    /**
     * The name of the division.
     */
    private String divisionName;

    /**
     * The ID of the country.
     */
    public int country_ID;

    /**
     * Constructs a new FirstLevelDivision object with the given parameters.
     *
     * @param divisionID    The ID of the division.
     * @param divisionName  The name of the division.
     * @param country_ID    The ID of the country.
     */
    public FirstLevelDivision(int divisionID, String divisionName, int country_ID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.country_ID = country_ID;
    }

    /**
     *
     * @return divisionID
     */
    public int getDivisionID() {

        return divisionID;
    }

    /**
     *
     * @return divisionName
     */
    public String getDivisionName() {

        return divisionName;
    }

    /**
     *
     * @return country_ID
     */
    public int getCountry_ID() {

        return country_ID;
    }
}
