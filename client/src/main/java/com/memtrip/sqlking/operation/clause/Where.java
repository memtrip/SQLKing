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
package com.memtrip.sqlking.operation.clause;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class Where<T> implements Clause {
    private String mRow;
    private Exp mExpression;
    private T mValue;

    public enum Exp {
        EQUAL_TO ("="),
        MORE_THAN (">"),
        MORE_THAN_OR_EQUAL_TO (">="),
        LESS_THAN ("<"),
        LESS_THAN_OR_EQUAL_TO ("<="),
        LIKE ("LIKE");

        private final String mValue;

        Exp(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public String getRow() {
        return mRow;
    }

    public Exp getExpression() {
        return mExpression;
    }

    public T getValue() {
        return mValue;
    }

    private Where(String row, Exp expression, T value) {
        mRow = row;
        mExpression = expression;
        mValue = value;
    }

    /**
     * Specifies a SQLite WHERE clause
     * @param row  The row to perform the clause on
     * @param expression The type of expression that will evaluate the value
     * @param value  The value being evaluated
     * @return Where clause
     */
    @SuppressWarnings("unchecked")
    public static Where where(String row, Exp expression, Object value) {
        return new Where(row, expression, value);
    }
}