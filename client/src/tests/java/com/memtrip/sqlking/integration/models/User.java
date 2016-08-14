package com.memtrip.sqlking.integration.models;

import com.memtrip.sqlking.common.Column;
import com.memtrip.sqlking.common.ForeignKey;
import com.memtrip.sqlking.common.Table;

@Table(
        foreignKeys = {
                @ForeignKey(
                        targetTable = "Log",
                        targetColumn = "id",
                        localColumn = "logId"
                )
        }
)
public class User  {
    @Column int id; // no index supplied for the @Test testNoUserIndexesAreCreated()
    @Column String username;
    @Column long timestamp;
    @Column boolean isRegistered;
    @Column byte[] profilePicture;
    @Column double rating;
    @Column int count;
    @Column int logId;
    @Column Log log;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newVal) {
        username = newVal;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long newVal) {
        timestamp = newVal;
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean newVal) {
        isRegistered = newVal;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] newVal) {
        profilePicture = newVal;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }
}