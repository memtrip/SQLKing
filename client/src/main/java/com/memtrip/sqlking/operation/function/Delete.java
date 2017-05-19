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

import com.memtrip.sqlking.database.Query;
import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.operation.clause.Clause;

import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Executes a Delete query against the SQLite database
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class Delete extends Query {
    private Clause[] mConditions;

    public Clause[] getConditions() {
        return mConditions;
    }

    private Delete(Clause[] conditions) {
        mConditions = conditions;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Clause[] mClause;

        private Builder() { }

        /**
         * Specify a Where clause for the Delete query
         * @param clause Where clause
         * @return Call Builder#execute or Builder#rx to run the query
         */
        public Builder where(Clause... clause) {
            mClause = clause;
            return this;
        }

        /**
         * Executes a Delete query
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         * @return The rows affected by the Delete query
         */
        public int execute(Class<?> classDef, SQLProvider sqlProvider) {
            return delete(
                    new Delete(mClause),
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