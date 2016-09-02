package com.memtrip.sqlking.integration.models;

import com.memtrip.sqlking.common.Column;
import com.memtrip.sqlking.common.Table;

@Table
public class Data {

    @Column String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
