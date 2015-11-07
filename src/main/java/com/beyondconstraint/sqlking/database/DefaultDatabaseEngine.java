/**
 * Copyright 2013-present beyond constraint.
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
package com.beyondconstraint.sqlking.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.beyondconstraint.sqlking.Model;
import com.beyondconstraint.sqlking.operation.clause.Clause;
import com.beyondconstraint.sqlking.operation.keyword.Limit;
import com.beyondconstraint.sqlking.operation.keyword.OrderBy;

/**
 * @author samkirton
 */
public class DefaultDatabaseEngine implements DatabaseEngine {
    private Database mDatabase;

    public DefaultDatabaseEngine(Database database) {
        mDatabase = database;
    }

    @Override
    public long insert(Model model, DatabaseController databaseController) throws SQLException {
        String tableName = databaseController.getTableName(model);
        ContentValues contentValues = databaseController.getContentValuesFromModel(model);
        return mDatabase.insert(tableName, contentValues);
    }

    @Override
    public long[] insert(Model[] models, DatabaseController databaseController) throws SQLException {
        if (models != null && models.length > 0) {
            InsertQuery[] insertQueries = new InsertQuery[models.length];

            for (int i = 0; i < models.length; i++) {
                insertQueries[i] = databaseController.createInsertQuery(models[i]);
            }

            return mDatabase.insertMultiple(insertQueries);
        } else {
            return null;
        }
    }

    @Override
    public int update(Class<? extends Model> classDef, ContentValues values, Clause[] clause, DatabaseController databaseController) {
        String tableName = databaseController.getTableName(classDef);

        return mDatabase.update(
                tableName,
                values,
                databaseController.getClause(clause),
                databaseController.getClauseArgs(clause)
        );
    }

    @Override
    public <T> T[] select(Class<? extends T> classDef, Clause[] clause, OrderBy orderBy, Limit limit, DatabaseController databaseController) {
        String tableName = databaseController.getTableName(classDef);
        String[] columns = databaseController.getSQLColumnNamesFromModel(classDef);

        Cursor cursor = mDatabase.query(tableName,
                columns,
                databaseController.getClause(clause),
                databaseController.getClauseArgs(clause),
                null,
                null,
                databaseController.getOrderBy(orderBy),
                databaseController.getLimit(limit)
        );

        return databaseController.retrieveSQLSelectResults(classDef, cursor);
    }

    @Override
    public long count(Class<? extends  Model> classDef, Clause[] clause, DatabaseController databaseController) {
        String tableName = databaseController.getTableName(classDef);

        return mDatabase.count(
                tableName,
                databaseController.getClause(clause),
                databaseController.getClauseArgs(clause)
        );
    }

    @Override
    public int delete(Class<? extends  Model> classDef, Clause[] clause, DatabaseController databaseController) {
        String tableName = databaseController.getTableName(classDef);

        int result = mDatabase.delete(
            tableName,
            databaseController.getClause(clause),
            databaseController.getClauseArgs(clause)
        );

        return result;
    }
}