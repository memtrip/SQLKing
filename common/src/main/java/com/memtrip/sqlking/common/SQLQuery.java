package com.memtrip.sqlking.common;

import android.database.Cursor;
import android.content.ContentValues;

public interface SQLQuery {
    String getTableName();
    String getTableInsertQuery();
    String[] buildUnionInsertQuery(Object[] models);
    String[] getColumnNames();
    ContentValues getContentValues(Object model);
    <T> T[] retrieveSQLSelectResults(Cursor cursor);
}
