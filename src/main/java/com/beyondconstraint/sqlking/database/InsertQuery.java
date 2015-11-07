package com.beyondconstraint.sqlking.database;

import android.content.ContentValues;

public class InsertQuery {
    private String mTableName;
    private ContentValues mContentValues;

    public String getTableName() {
        return mTableName;
    }

    public ContentValues getContentValues() {
        return mContentValues;
    }

    public InsertQuery(String tableName, ContentValues contentValues) {
        mTableName = tableName;
        mContentValues = contentValues;
    }
}
