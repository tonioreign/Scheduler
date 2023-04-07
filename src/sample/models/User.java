package sample.models;

import java.time.LocalDateTime;

public class User {
    /**THE USERS ID*/
    int userId;
    /**THE USERS USERNAME*/
    String userName;
    /**THE USERS PASSWORD*/
    String password;
    /**WHEN THE USER WAS CREATED*/
    LocalDateTime createDate;
    /**WHO THE USER WAS CREATED BY*/
    String createdBy;
    /**LAST TIME THE USER WAS UPDATED*/
    LocalDateTime lastUpdate;
    /**WHO THE USER WAS UPDATED BY*/
    String lastUpdatedBy;

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
