package sample.models;

public class Contacts {
    /**
     * The ID of the contact.
     * This variable is public, use with caution.
     *
     * @author Antonio Jenkins
     */
    public int contactID;

    /**
     * The name of the contact.
     * This variable is public, use with caution.
     */
    public String contactName;

    /**
     * The email address of the contact.
     * This variable is public, use with caution.
     */
    public String contactEmail;

    /**
     * Constructs a new Contacts object with the given parameters.
     *
     * @param contactID     The ID of the contact.
     * @param contactName   The name of the contact.
     * @param contactEmail  The email address of the contact.
     */
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }


    /**GETTERS AND SETTERS
     * @return*/
    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
