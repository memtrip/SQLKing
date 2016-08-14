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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.memtrip.sqlking.common.Resolver;
import com.memtrip.sqlking.operation.clause.Clause;
import com.memtrip.sqlking.operation.join.Join;
import com.memtrip.sqlking.operation.keyword.Limit;
import com.memtrip.sqlking.operation.keyword.OrderBy;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class SQLProvider {
    private SQLiteDatabase mDatabase;
    private Resolver mResolver;
    private ClauseHelper mClauseHelper;

    protected Resolver getResolver() {
        return mResolver;
    }

    protected SQLProvider(SQLiteDatabase database, Resolver resolver) {
        mDatabase = database;
        mResolver = resolver;
        mClauseHelper = new ClauseHelper();
    }

    protected void insertMultiple(String[] unionInsertQuery) {
        mDatabase.beginTransaction();

        for (String query : unionInsertQuery) {
            Cursor cursor = mDatabase.rawQuery(query, null);
            cursor.moveToLast();
            cursor.close();
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    protected int update(String tableName, ContentValues values, Clause[] clause) {
        return mDatabase.update(
                tableName,
                values,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }

    protected Cursor query(String tableName, String[] columns, Clause[] clause, Join[] joins,
                           String groupBy, String having, OrderBy orderBy, Limit limit) {

        if (joins != null && joins.length > 0) {
            try {
                String joinQuery = mClauseHelper.buildJoinQuery(
                        columns,
                        joins,
                        tableName,
                        clause,
                        orderBy,
                        limit,
                        mResolver
                );

                return mDatabase.rawQuery(joinQuery, mClauseHelper.getClauseArgs(clause));
            } catch (Exception e) {
                throw new SQLException(e.getMessage());
            }
        } else {
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
    }

    protected int delete(String tableName, Clause[] clause) {
        return mDatabase.delete(
                tableName,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }

    protected long count(String tableName, Clause[] clause) {
        return DatabaseUtils.queryNumEntries(
                mDatabase,
                tableName,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }

    protected Cursor rawQuery(String sql) {
        return mDatabase.rawQuery(sql, null);
    }
}