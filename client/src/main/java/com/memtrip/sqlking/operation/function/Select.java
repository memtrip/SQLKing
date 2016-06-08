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
import com.memtrip.sqlking.operation.join.Join;
import com.memtrip.sqlking.operation.keyword.Limit;
import com.memtrip.sqlking.operation.keyword.OrderBy;

import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Executes a Select query against the SQLite database
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class Select extends Query {
    private Clause[] mClause;
    private Join[] mJoin;
    private OrderBy mOrderBy;
    private Limit mLimit;

    public Clause[] getClause() {
        return mClause;
    }

    public Join[] getJoin() {
        return mJoin;
    }

    public OrderBy getOrderBy() {
        return mOrderBy;
    }

    public Limit getLimit() {
        return mLimit;
    }

    private Select(Clause[] clause, Join[] join, OrderBy orderBy, Limit limit) {
        mClause = clause;
        mJoin = join;
        mOrderBy = orderBy;
        mLimit = limit;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Clause[] mClause;
        private Join[] mJoin;
        private OrderBy mOrderBy;
        private Limit mLimit;

        private Builder() { }

        /**
         * Specify a Where clause for the Select query
         * @param clause Where clause
         * @return Call Builder#execute, Builder#rx, or Builder#rxSingle to run the query
         */
        public Builder where(Clause... clause) {
            mClause = clause;
            return this;
        }

        public Builder join(Join... joins) {
            mJoin = joins;
            return this;
        }

        /**
         * Specify an Order By clause for the Select query
         * @param column The column to use with the Order By clause
         * @param order The direction of the Order By clause
         * @return Call Builder#executem Builder#rx or Builder#rxSingle to run the query
         */
        public Builder orderBy(String column, OrderBy.Order order) {
            mOrderBy = new OrderBy(column, order);
            return this;
        }

        /**
         * Specify a Limit clause for the Select query
         * @param start The starting index to select from
         * @param end The ending index to select from
         * @return Call Builder#execute, Builder#rx or Builder#rxSingle to run the query
         */
        public Builder limit(int start, int end) {
            mLimit = new Limit(start, end);
            return this;
        }

        /**
         * Executes a Select query
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         * @param <T> The model object returned from the query
         * @return The rows returned by the Select query
         */
        public <T> T[] execute(Class<T> classDef, SQLProvider sqlProvider) {
            return select(
                    new Select(mClause, mJoin, mOrderBy, mLimit),
                    classDef,
                    sqlProvider
            );
        }

        /**
         * Executes a Select query that expects a single result
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         * @param <T> The model object returned from the query
         * @return The row returned by the Select query
         */
        public <T> T executeOne(Class<T> classDef, SQLProvider sqlProvider) {
            return selectSingle(
                    new Select(mClause, mJoin, mOrderBy, mLimit),
                    classDef,
                    sqlProvider
            );
        }

        /**
         * Executes a Select query
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         * @param <T> The model object returned from the query
         * @return An RxJava Observable
         */
        public <T> Observable<T[]> rx(final Class<T> classDef, final SQLProvider sqlProvider) {
            return wrapRx(new Callable<T[]>() {
                @Override
                public T[] call() throws Exception {
                    return execute(classDef, sqlProvider);
                }
            });
        }

        /**
         * Executes a Select query that expects a single result
         * @param classDef The class definition that the query should run on
         * @param sqlProvider Where the magic happens!
         * @param <T> The model object returned from the query
         * @return An RxJava Observable
         */
        public <T> Observable<T> rxOne(final Class<T> classDef, final SQLProvider sqlProvider) {
            return wrapRx(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return executeOne(classDef, sqlProvider);
                }
            });
        }
    }
}