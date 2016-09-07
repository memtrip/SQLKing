package com.memtrip.sqlking.preprocessor;

import com.memtrip.sqlking.common.Column;
import com.memtrip.sqlking.common.Table;

@Table
public class Post {
    @Column(primary_key = true) String id;
    @Column String title;
    @Column byte[] blob;
    @Column long timestamp;
    @Column User user;
    @Column Data data;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}