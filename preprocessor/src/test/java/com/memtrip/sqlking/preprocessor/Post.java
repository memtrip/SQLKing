package com.memtrip.sqlking.preprocessor;

import com.memtrip.sqlking.common.Member;
import com.memtrip.sqlking.common.Table;

@Table
public class Post implements Model {
    @Member private String id;
    @Member private String title;
    @Member private byte[] blob;
    @Member private long timestamp;

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
