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

/**
 * Executes an Insert query against the SQLite database
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class Insert extends Query {
    private Object[] mModels;

    public Object[] getModels() {
        return mModels;
    }

    private Insert(Object... models) {
        mModels = models;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Object[] mValues;

        private Builder() { }

        /**
         * Specify the values for the Insert query
         * @param values The values that are being inserted
         * @return Call Builder#execute to run the query
         */
        public Builder values(Object... values) {
            mValues = values;
            return this;
        }

        /**
         * Executes an Insert query
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         */
        public void execute(Class<? extends Object> classDef, SQLProvider sqlProvider) {
            insert(
                    new Insert(mValues),
                    classDef,
                    sqlProvider
            );
        }
    }
}