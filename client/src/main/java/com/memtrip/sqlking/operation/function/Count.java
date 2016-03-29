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

/**
 * Executes a Count query against the SQLite database
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class Count extends Query {
    private Clause[] mClause;

    public Clause[] getClause() {
        return mClause;
    }

    public Count(Clause[] clause) {
        mClause = clause;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Clause[] mClause;

        private Builder() { }

        /**
         * Specify a Where clause for the Count query
         * @param clause Where clause
         * @return Call Builder#execute to run the query
         */
        public Builder where(Clause... clause) {
            mClause = clause;
            return this;
        }

        /**
         * Execute a Count query
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         * @return The row count returned by the query
         */
        public long execute(Class<?> classDef, SQLProvider sqlProvider) {
            return count(
                    new Count(mClause),
                    classDef,
                    sqlProvider
            );
        }
    }
}