package sample.models;

import java.time.LocalDateTime;

public class Country {

    /**
     * The ID of the country.
     */
    private int countryId;

    /**
     * The name of the country.
     */
    private String countryName;

    /**
     * Constructs a new Country object with the given parameters.
     *
     * @param countryId    The ID of the country.
     * @param countryName  The name of the country.
     */
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }


    /**GETTERS AND SETTERS*/
    public int getCountryID() {
        return countryId;
    }

    public void setCountryID(int contryId) {
        this.countryId = contryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
