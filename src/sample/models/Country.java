package sample.models;

import java.time.LocalDateTime;

public class Country {
    private int contryId;
    private String countryName;

    /**
     *
     * @param contryId
     * @param countryName
     */
    public Country(int contryId, String countryName) {
        this.contryId = contryId;
        this.countryName = countryName;
    }

    /**GETTERS AND SETTERS*/
    public int getCountryID() {
        return contryId;
    }

    public void setCountryID(int contryId) {
        this.contryId = contryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
