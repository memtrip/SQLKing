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
package com.memtrip.sqlking.operation.function;

import android.content.ContentValues;

import com.memtrip.sqlking.database.Query;
import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.operation.clause.Clause;
import com.memtrip.sqlking.operation.clause.Where;

import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Executes an Update query against the SQLite database
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class Update extends Query {
    private ContentValues mContentValues;
    private Clause[] mConditions;

    public ContentValues getContentValues() {
        return mContentValues;
    }

    public Clause[] getConditions() {
        return mConditions;
    }

    public Update(ContentValues contentValues, Clause[] conditions) {
        mContentValues = contentValues;
        mConditions = conditions;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private ContentValues mValues;
        private Clause[] mClause;

        /**
         * Specify a Where clause for the Update query
         * @param clause Where clause
         * @return Call Builder#execute or Builder#rx to run the query
         */
        public Builder where(Where... clause) {
            mClause = clause;
            return this;
        }

        /**
         * Specify the values for the Update query
         * @param values The values that are being updated
         * @return Call Builder#execute or Builder#rx to run the query
         */
        public Builder values(ContentValues values) {
            mValues = values;
            return this;
        }

        /**
         * Executes an Update query
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         * @return The rows affected by the Update query
         */
        public int execute(Class<?> classDef, SQLProvider sqlProvider) {
            return update(
                    new Update(mValues, mClause),
                    classDef,
                    sqlProvider
            );
        }

        public Single<Integer> rx(final Class<?> classDef, final SQLProvider sqlProvider) {
            return wrapSingle(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return execute(classDef, sqlProvider);
                }
            });
        }
    }
}