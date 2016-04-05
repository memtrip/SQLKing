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
package com.memtrip.sqlking.operation.keyword;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class OrderBy {
    private String mField;
    private Order mOrder;

    public enum Order {
        ASC ("ASC"),
        DESC ("DESC"),
        RANDOM ("RANDOM");

        private final String mValue;

        Order(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public String getField() {
        return mField;
    }

    public Order getOrder() {
        return mOrder;
    }

    public OrderBy(String field, Order order) {
        mField = field;
        mOrder = order;
    }
}
