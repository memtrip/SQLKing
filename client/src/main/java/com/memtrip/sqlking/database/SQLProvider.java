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

import android.database.Cursor;

import com.memtrip.sqlking.common.Resolver;
import com.memtrip.sqlking.common.SQLQuery;
import com.memtrip.sqlking.operation.function.Count;
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;
import com.memtrip.sqlking.operation.function.Update;

/**
 * @author Samuel Kirton <a href="mailto:sam@memtrip.com" />
 */
public class SQLProvider {
    private SQLDatabase mDatabase;
    private Resolver mResolver;

    public Resolver getResolver() {
        return mResolver;
    }

    public SQLProvider(SQLDatabase database, Resolver resolver) {
        mDatabase = database;
        mResolver = resolver;
    }

    public void insert(Insert insert, SQLQuery sqlQuery) {
        if (insert.getModels() != null && insert.getModels().length > 0) {
            String[] unionInsert = sqlQuery.buildUnionInsertQuery(insert.getModels());
            mDatabase.insertMultiple(unionInsert);
        }
    }

    public <T> T[] select(Select select, SQLQuery sqlQuery) {
        Cursor cursor = mDatabase.query(
                sqlQuery.getTableName(),
                sqlQuery.getColumnNames(),
                select.getClause(),
                null,
                null,
                select.getOrderBy(),
                select.getLimit()
        );

        return sqlQuery.retrieveSQLSelectResults(cursor);
    }

    public <T> T selectSingle(Select select, SQLQuery sqlQuery) {
        T[] results = select(select, sqlQuery);

        if (results != null && results.length > 0) {
            return results[0];
        } else {
            return null;
        }
    }

    public boolean update(Update update, SQLQuery sqlQuery) {
        int result = mDatabase.update(
                sqlQuery.getTableName(),
                update.getContentValues(),
                update.getConditions()
        );

        return result >= 1;
    }

    public long count(Count count, SQLQuery sqlQuery) {
        return mDatabase.count(
                sqlQuery.getTableName(),
                count.getClause()
        );
    }

    public boolean delete(Delete delete, SQLQuery sqlQuery) {
        int result = mDatabase.delete(
                sqlQuery.getTableName(),
                delete.getConditions()
        );

        return result >= 1;
    }
}