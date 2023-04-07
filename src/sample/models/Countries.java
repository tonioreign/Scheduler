package sample.models;

import java.time.LocalDateTime;

public class Countries {
    /**THE COUNTRIES ID*/
    int countryId;
    /**THE COUNTRIES NAME*/
    String country;
    /**CREATION DATE*/
    LocalDateTime createDate;
    /**WHO CREATED THE COUNTRY*/
    String createdBy;
    /**LAST TIME COUNTRY UPDATED*/
    LocalDateTime lastUpdated;
    /**WHO UPDATED COUNTRY*/
    String lastUpdatedBy;

    public Countries(String name) {
        this.country = name;
    }

    /**GETTERS AND SETTERS*/
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
