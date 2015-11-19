package com.memtrip.sqlking.preprocessor;

import com.memtrip.sqlking.common.Member;
import com.memtrip.sqlking.common.Table;

@Table
public class User implements Model {
    @Member private String username;
    @Member private long timestamp;
    @Member private boolean isRegistered;
    @Member private byte[] profilePicture;

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
}