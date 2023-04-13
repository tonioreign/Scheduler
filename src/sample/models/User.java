package sample.models;

import java.time.LocalDateTime;

public class User {
    /**
     * The ID of the user.
     */
    private int userId;

    /**
     * The username of the user.
     */
    private String userName;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The date and time when the user was created.
     */
    private LocalDateTime createDate;

    /**
     * The username of the user who created this user.
     */
    private String createdBy;

    /**
     * The date and time of the last update made to this user.
     */
    private LocalDateTime lastUpdate;

    /**
     * The username of the user who last updated this user.
     */
    private String lastUpdatedBy;

    /**
     * Constructs a new User object with the given user ID, username, and password.
     *
     * @param userId The ID of the user.
     * @param userName The username of the user.
     * @param password The password of the user.
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**GETTERS AND SETTERS*/
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
