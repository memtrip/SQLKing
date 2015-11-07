package com.beyondconstraint.sqlking.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

public class SQLDatabase implements Database {
    private android.database.sqlite.SQLiteDatabase mDatabase;

    public SQLDatabase(android.database.sqlite.SQLiteDatabase database) {
        mDatabase = database;
    }

    @Override
    public long insert(String tableName, ContentValues contentValues) {
        return mDatabase.insertOrThrow(tableName, null, contentValues);
    }

    @Override
    public long[] insertMultiple(InsertQuery[] insertQueries) {
        long[] results = new long[insertQueries.length];

        mDatabase.beginTransaction();

        for (int i = 0; i < insertQueries.length; i++) {
            results[i] = mDatabase.insert(insertQueries[i].getTableName(), null, insertQueries[i].getContentValues());
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();


        return results;
    }

    @Override
    public int update(String tableName, ContentValues values, String clause, String[] clauseArgs) {
        return mDatabase.update(tableName, values, clause, clauseArgs);
    }

    @Override
    public Cursor query(String tableName, String[] columns, String clause, String[] clauseArgs, String groupBy, String having, String orderBy, String limit) {
        return mDatabase.query(tableName, columns, clause, clauseArgs, groupBy, having, orderBy, limit);
    }

    @Override
    public int delete(String tableName, String clause, String[] clauseArgs) {
        return mDatabase.delete(tableName, clause, clauseArgs);
    }

    @Override
    public long count(String tableName, String clause, String[] clauseArgs) {
        return DatabaseUtils.queryNumEntries(mDatabase, tableName, clause, clauseArgs);
    }
}