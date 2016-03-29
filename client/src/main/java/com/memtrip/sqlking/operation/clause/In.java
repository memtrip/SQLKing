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
public class In <T> implements Clause {
    private String mRow;
    private T[] mValues;

    public String getRow() {
        return mRow;
    }

    public T[] getValues() {
        return mValues;
    }

    private In(String row, T... values) {
        mRow = row;
        mValues = values;
    }

    /**
     * Specifies a SQLite IN operator
     * @param row  The row to perform the operation on
     * @param values The values of the in operator
     * @return  In operator
     */
    public static In in(String row, Object... values) {
        return new In(row, values);
    }
}