package sample.models;

import java.time.LocalDateTime;

public class Customer {
    /**THE CUSTOMERS ID*/
    private int customerId;
    /**THE CUSTOMERS NAME*/
    private String customerName;
    /**THE CUSTOMERS ADDRESS*/
    private int addressId;
    /**THE CUSTOMERS CREATION DATE*/
    private LocalDateTime createDate;
    /**WHO THE CREATED CUSTOMER*/
    private String createdBy;
    /**CUSTOMERS LAST UPDATE*/
    private LocalDateTime lastUpdate;
    /**WHO UPDATED CUSTOMER*/
    private String lastUpdateBy;

    public Customer(String name) {
        this.customerName = name;
    }

    /**GETTERS AND SETTERS*/
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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
