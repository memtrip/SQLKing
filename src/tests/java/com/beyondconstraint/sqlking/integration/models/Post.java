package com.beyondconstraint.sqlking.integration.models;

import com.beyondconstraint.sqlking.Model;

public class Post implements Model {
    private String id;
    private String title;
    private byte[] blob;
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String newVal) {
        id = newVal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newVal) {
        title = newVal;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] newVal) {
        blob = newVal;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long newVal) {
        timestamp = newVal;
    }
}