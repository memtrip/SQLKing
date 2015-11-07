package com.beyondconstraint.sqlking.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface Database {
    long insert(String tableName, ContentValues contentValues);
    long[] insertMultiple(InsertQuery[] insertQueries);
    int update(String tableName, ContentValues values, String clause, String[] clauseArgs);
    Cursor query(String tableName, String[] columns, String clause, String[] clauseArgs, String groupBy, String having, String orderBy, String limit);
    int delete(String tableName, String clause, String[] clauseArgs);
    long count(String tableName, String clause, String[] clauseArgs);
}