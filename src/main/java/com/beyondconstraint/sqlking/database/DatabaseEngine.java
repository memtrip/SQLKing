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
import android.database.SQLException;

import com.beyondconstraint.sqlking.Model;
import com.beyondconstraint.sqlking.operation.clause.Clause;
import com.beyondconstraint.sqlking.operation.keyword.Limit;
import com.beyondconstraint.sqlking.operation.keyword.OrderBy;

/**
 * @author samkirton
 */
public interface DatabaseEngine {
    long insert(Model model, DatabaseController databaseController) throws SQLException;
    long[] insert(Model[] models, DatabaseController databaseController) throws SQLException;
    int update(Class<? extends Model> classDef, ContentValues values, Clause[] clause,  DatabaseController databaseController);
    <T> T[] select(Class<? extends T> classDef, Clause[] clause, OrderBy orderBy, Limit limit, DatabaseController databaseController);
    long count(Class<? extends Model> classDef, Clause[] clause, DatabaseController databaseController);
    int delete(Class<? extends Model> classDef, Clause[] clause, DatabaseController databaseController);
}