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

import com.beyondconstraint.sqlking.operation.function.Count;
import com.beyondconstraint.sqlking.operation.function.Delete;
import com.beyondconstraint.sqlking.operation.function.Insert;
import com.beyondconstraint.sqlking.operation.function.Select;
import com.beyondconstraint.sqlking.operation.function.Update;

public class SQLProvider {
    private DatabaseEngine mDatabaseEngine;
    private DatabaseController mDatabaseController;

    public SQLProvider(DatabaseEngine databaseEngine) {
        mDatabaseEngine = databaseEngine;
        mDatabaseController = new DatabaseController();
    }

    public long[] insert(Insert insert) {
        return mDatabaseEngine.insert(insert.getModels(), mDatabaseController);
    }

    public <T> T[] select(Select select, Class<T> classDef) {
        return mDatabaseEngine.select(
                classDef,
                select.getClause(),
                select.getOrderBy(),
                select.getLimit(),
                mDatabaseController
        );
    }

    public <T> T selectSingle(Select select, Class<T> classDef) {
        T[] results = mDatabaseEngine.select(
                classDef,
                select.getClause(),
                select.getOrderBy(),
                select.getLimit(),
                mDatabaseController
        );

        if (results != null && results.length > 0) {
            return results[0];
        } else {
            return null;
        }
    }

    public boolean update(Update update) {
        int result = mDatabaseEngine.update(update.getClassDef(), update.getContentValues(), update.getConditions(), mDatabaseController);
        return result >= 1;
    }

    public long count(Count count) {
        return mDatabaseEngine.count(count.getClassDef(), count.getClause(), mDatabaseController);
    }

    public boolean delete(Delete delete) {
        int result = mDatabaseEngine.delete(delete.getClassDef(), delete.getConditions(), mDatabaseController);
        return result >= 1;
    }
}