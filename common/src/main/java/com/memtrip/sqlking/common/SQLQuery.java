package com.memtrip.sqlking.common;

import android.content.ContentValues;
import android.database.Cursor;

public interface SQLQuery {
    String getTableName();
    String getTableInsertQuery();
    String[] getIndexNames();
    String getCreateIndexQuery();
    String[] buildUnionInsertQuery(Object[] models);
    String[] getColumnNames();
    ContentValues getContentValues(Object model);
    <T> T[] retrieveSQLSelectResults(Cursor cursor);
}