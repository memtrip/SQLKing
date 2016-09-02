package com.memtrip.sqlking.integration.models;

import com.memtrip.sqlking.common.Column;
import com.memtrip.sqlking.common.ForeignKey;
import com.memtrip.sqlking.common.Table;

@Table(
        foreignKeys = {
                @ForeignKey(
                        targetTable = "User",
                        targetColumn = "id",
                        localColumn = "userId"
                )
        }
)
public class Post {
    @Column(index = true) int id;
    @Column String title;
    @Column String body;
    @Column long timestamp;
    @Column int userId;
    @Column User user;
    @Column Data data;

    public int getId() {
        return id;
    }

    public void setId(int newVal) {
        id = newVal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newVal) {
        title = newVal;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long newVal) {
        timestamp = newVal;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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