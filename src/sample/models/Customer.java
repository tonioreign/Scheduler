package sample.models;

/**
 * This class represents a customer.
 */
public class Customer {
    /**
     * The name of the division.
     */
    private String divisionName;

    /**
     * The ID of the customer.
     */
    private int customerID;

    /**
     * The name of the customer.
     */
    private String customerName;

    /**
     * The address of the customer.
     */
    private String customerAddress;

    /**
     * The postal code of the customer.
     */
    private String customerPostalCode;

    /**
     * The phone number of the customer.
     */
    private String customerPhone;

    /**
     * The ID of the division.
     */
    private int divisionID;

    /**
     * Constructs a new Customer object with the given parameters.
     *
     * @param customerID         The ID of the customer.
     * @param customerName       The name of the customer.
     * @param customerAddress    The address of the customer.
     * @param customerPostalCode The postal code of the customer.
     * @param customerPhone      The phone number of the customer.
     * @param divisionID         The ID of the division.
     * @param divisionName       The name of the division.
     */
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode,
                    String customerPhone, int divisionID, String divisionName) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }

    /**
     * @return customerID
     */
    public Integer getCustomerID() {

        return customerID;
    }

    /**
     * @param customerID
     */
    public void setCustomerID(Integer customerID) {

        this.customerID = customerID;
    }

    /**
     * @return customerName
     */
    public String getCustomerName() {

        return customerName;
    }

    /**
     * @param customerName
     */
    public void setCustomerName(String customerName) {

        this.customerName = customerName;
    }

    /**
     * @return customerAddress
     */
    public String getCustomerAddress() {

        return customerAddress;
    }

    /**
     * @param address
     */
    public void setCustomerAddress(String address) {

        this.customerAddress = address;
    }

    /**
     * @return customerPostalCode
     */
    public String getCustomerPostalCode() {

        return customerPostalCode;
    }

    /**
     * @param postalCode
     */
    public void setCustomerPostalCode(String postalCode) {

        this.customerPostalCode = postalCode;
    }

    /**
     * @return customerPhoneNumber
     */
    public String getCustomerPhone() {

        return customerPhone;
    }

    /**
     * @param phone
     */
    public void setCustomerPhone(String phone) {

        this.customerPhone = phone;
    }

    /**
     * @return divisionID
     */
    public Integer getCustomerDivisionID() {

        return divisionID;
    }

    /**
     * @return divisionID
     */
    public String getDivisionName() {

        return divisionName;
    }

    /**
     * @param divisionID
     */
    public void setCustomerDivisionID(Integer divisionID) {

        this.divisionID = divisionID;
    }
}
