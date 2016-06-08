package com.memtrip.sqlking.preprocessor.processor.column;

import com.memtrip.sqlking.preprocessor.processor.model.ForeignKey;
import com.memtrip.sqlking.preprocessor.processor.model.Table;

import java.util.List;

public class Column {
    private String mName;
    private String mClassName;
    private String mType;
    private boolean mIsIndex;
    private ForeignKey mForeignKey;

    public String getName() {
        return mName;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getType() {
        return mType;
    }

    /**
     * Used in resources/Q.java (freemarker file)
     */
    public boolean isIndex() {
        return mIsIndex;
    }

    public ForeignKey getForeignKey() {
        return mForeignKey;
    }

    public boolean hasForeignKey() {
        return mForeignKey != null;
    }

    void setName(String newVal) {
        mName = newVal;
    }

    void setClassName(String newVal) {
        mClassName = newVal;
    }

    void setType(String newVal) {
        mType = newVal;
    }

    void setIsIndex(boolean newVal) {
        mIsIndex = newVal;
    }

    void setForeignKey(ForeignKey newVal) {
        mForeignKey = newVal;
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