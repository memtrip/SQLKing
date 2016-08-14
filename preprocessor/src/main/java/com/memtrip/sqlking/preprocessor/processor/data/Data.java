package com.memtrip.sqlking.preprocessor.processor.data;

import java.util.List;

public class Data {
    private List<Table> mTables;

    public List<Table> getTables() {
        return mTables;
    }

    public void setTables(List<Table> newVal) {
        mTables = newVal;
    }
}