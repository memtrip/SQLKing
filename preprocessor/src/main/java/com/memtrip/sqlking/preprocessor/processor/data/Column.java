package com.memtrip.sqlking.preprocessor.processor.data;

import java.util.List;

public class Column {
    private String mName;
    private String mClassName;
    private String mType;
    private boolean mIsIndex;
    private boolean mPrimaryKey;
    private boolean mHasAutoIncrement;

    public String getName() {
        return mName;
    }

    public void setName(String newVal) {
        mName = newVal;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String newVal) {
        mClassName = newVal;
    }

    public String getType() {
        return mType;
    }

    public void setType(String newVal) {
        mType = newVal;
    }

    /**
     * (Used in Q.java freemarker template)
     */
    public boolean isIndex() {
        return mIsIndex;
    }

    public void setIsIndex(boolean newVal) {
        mIsIndex = newVal;
    }

    public boolean hasPrimaryKey() {
        return mPrimaryKey;
    }

    public void setHasPrimaryKey(boolean newVal) {
        mPrimaryKey = newVal;
    }

    public boolean hasAutoIncrement() {
        return mHasAutoIncrement;
    }

    public void setHasAutoIncrement(boolean newVal) {
        mHasAutoIncrement = newVal;
    }

    public Table getRootTable(List<Table> tables) {
        if (isJoinable(tables)) {
            for (Table table : tables) {
                if (table.getType().equals(mType)) {
                    return table;
                }
            }
        }

        throw new IllegalStateException("Only joinable columns can call getRootTable");
    }

    public boolean isJoinable(List<Table> tables) {
        for (Table table : tables) {
            if (table.getName().toLowerCase().equals(mClassName.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}