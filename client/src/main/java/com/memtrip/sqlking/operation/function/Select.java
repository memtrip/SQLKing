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

import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.operation.clause.Clause;
import com.memtrip.sqlking.operation.keyword.Limit;
import com.memtrip.sqlking.operation.keyword.OrderBy;

/**
 * @author Samuel Kirton <a href="mailto:sam@memtrip.com" />
 */
public class Select <T> {
    private Clause[] mClause;
    private OrderBy mOrderBy;
    private Limit mLimit;

    public Clause[] getClause() {
        return mClause;
    }

    public OrderBy getOrderBy() {
        return mOrderBy;
    }

    public Limit getLimit() {
        return mLimit;
    }

    private Select(Clause[] clause, OrderBy orderBy, Limit limit) {
        mClause = clause;
        mOrderBy = orderBy;
        mLimit = limit;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Clause[] mClause;
        private OrderBy mOrderBy;
        private Limit mLimit;

        private Builder() { }

        public Builder where(Clause... conditions) {
            mClause = conditions;
            return this;
        }

        public Builder orderBy(String field, OrderBy.Order order) {
            mOrderBy = new OrderBy(field, order);
            return this;
        }

        public Builder limit(int start, int end) {
            mLimit = new Limit(start, end);
            return this;
        }

        public <T> T[] execute(Class<T> classDef, SQLProvider sqlProvider) {
            return sqlProvider.select(new Select(mClause, mOrderBy, mLimit), sqlProvider.getResolver().getSQLQuery(classDef));
        }

        public <T> T executeSingle(Class<T> classDef, SQLProvider sqlProvider) {
            return sqlProvider.selectSingle(
                    new Select(mClause, mOrderBy, mLimit),
                    sqlProvider.getResolver().getSQLQuery(classDef)
            );
        }
    }
}