package com.memtrip.sqlking.preprocessor.processor.data;

public class ForeignKey {
    private String mTable;
    private String mThisColumn;
    private String mForeignColumn;

    public String getTable() {
        return mTable;
    }

    public void setTable(String newVal) {
        mTable = newVal;
    }

    public String getThisColumn() {
        return mThisColumn;
    }

    public void setLocalColumn(String newVal) {
        mThisColumn = newVal;
    }

    public String getForeignColumn() {
        return mForeignColumn;
    }

    public void setTargetColumn(String newVal) {
        mForeignColumn = newVal;
    }
}