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

import com.memtrip.sqlking.Model;
import com.memtrip.sqlking.database.SQLProvider;

/**
 * @author Samuel Kirton <a href="mailto:sam@memtrip.com" />
 */
public class Insert {
    private Model[] mModels;

    public Model[] getModels() {
        return mModels;
    }

    private Insert(Model... models) {
        mModels = models;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Model[] mModels;

        private Builder() { }

        public Builder values(Model... models) {
            mModels= models;
            return this;
        }

        public void execute(Class<? extends Model> classDef, SQLProvider sqlProvider) {
            sqlProvider.insert(
                    new Insert(mModels),
                    sqlProvider.getResolver().getSQLQuery(classDef)
            );
        }
    }
}