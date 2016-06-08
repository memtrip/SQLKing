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
package com.memtrip.sqlking.operation.join;

import com.memtrip.sqlking.operation.clause.Clause;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
@SuppressWarnings("unchecked")
public class CrossInnerJoin<J> extends Join {

    public CrossInnerJoin(Class<J> table, Join join, Clause... clauses) {
        super(table, join, clauses);
    }

    public static CrossInnerJoin crossInnerJoin(Class<?> table, Clause... clauses) {
        return new CrossInnerJoin(table, null, clauses);
    }

    public static CrossInnerJoin crossInnerJoin(Class<?> table, Join join, Clause... clauses) {
        return new CrossInnerJoin(table, join, clauses);
    }
}