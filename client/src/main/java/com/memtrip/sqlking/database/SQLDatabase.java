/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memtrip.sqlking.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.memtrip.sqlking.operation.clause.Clause;
import com.memtrip.sqlking.operation.keyword.Limit;
import com.memtrip.sqlking.operation.keyword.OrderBy;

/**
 * @author Samuel Kirton <a href="mailto:sam@memtrip.com" />
 */
public class SQLDatabase {
    private SQLiteDatabase mDatabase;
    private ClauseHelper mClauseHelper;

    public SQLDatabase(SQLiteDatabase database) {
        mDatabase = database;
        mClauseHelper = new ClauseHelper();
    }

    public void insertMultiple(String[] unionInsertQuery) {
        mDatabase.beginTransaction();

        for (String query : unionInsertQuery) {
            Cursor cursor = mDatabase.rawQuery(query, null);
            cursor.moveToLast();
            cursor.close();
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public int update(String tableName, ContentValues values, Clause[] clause) {
        return mDatabase.update(
                tableName,
                values,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }

    public Cursor query(String tableName, String[] columns, Clause[] clause, String groupBy, String having, OrderBy orderBy, Limit limit) {
        return mDatabase.query(
                tableName,
                columns,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause),
                groupBy,
                having,
                mClauseHelper.getOrderBy(orderBy),
                mClauseHelper.getLimit(limit)
        );
    }

    public int delete(String tableName, Clause[] clause) {
        return mDatabase.delete(
                tableName,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }

    public long count(String tableName, Clause[] clause) {
        return DatabaseUtils.queryNumEntries(
                mDatabase,
                tableName,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }
}