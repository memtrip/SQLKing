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
public class On implements Clause {
    private String mColumn1;
    private String mColumn2;

    public String getColumn1() {
        return mColumn1;
    }

    public String getColumn2() {
        return mColumn2;
    }

    public On(String column1, String column2) {
        mColumn1 = column1;
        mColumn2 = column2;
    }

    @SuppressWarnings("unchecked")
    public static On on(String column1, String column2) {
        return new On(column1, column2);
    }
}