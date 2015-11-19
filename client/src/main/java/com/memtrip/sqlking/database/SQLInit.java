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
package com.memtrip.sqlking.database;

import android.content.Context;

import com.memtrip.sqlking.Model;
import com.memtrip.sqlking.common.Resolver;
import com.memtrip.sqlking.common.SQLQuery;

/**
 * Build the SQL database based on the provided models
 * @author Samuel Kirton <a href="mailto:sam@memtrip.com" />
 */
public class SQLInit {

    public static SQLDatabase createDatabase(String name,
                                             int version,
                                             Resolver resolver,
                                             Context context,
                                             Class<? extends Model> ... modelClassDefs) {

        String[] schemaArray = new String[modelClassDefs.length];
        String[] tableNameArray = new String[modelClassDefs.length];

        for (int i = 0; i < modelClassDefs.length; i++) {
            SQLQuery sqlQuery = resolver.getSQLQuery(modelClassDefs[i]);
            schemaArray[i] = sqlQuery.getTableInsertQuery();
            tableNameArray[i] = sqlQuery.getTableName();
        }

        SQLOpen sqlOpen = new SQLOpen(name, version, schemaArray, tableNameArray, context);
        return new SQLDatabase(sqlOpen.getDatabase());
    }
}