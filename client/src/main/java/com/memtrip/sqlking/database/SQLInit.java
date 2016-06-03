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

import com.memtrip.sqlking.common.Resolver;
import com.memtrip.sqlking.common.SQLQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Build the SQL database based on the provided models
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class SQLInit {

    public static SQLProvider createDatabase(String name,
                                             int version,
                                             Resolver resolver,
                                             Context context,
                                             Class<?> ... modelClassDef) {

        int modelCount = modelClassDef.length;

        String[] schemaArray = new String[modelCount];
        String[] tableNameArray = new String[modelCount];
        String[] createIndexArray = new String[modelCount];
        List<String> indexNameArray = new ArrayList<>();

        for (int i = 0; i < modelClassDef.length; i++) {
            SQLQuery sqlQuery = resolver.getSQLQuery(modelClassDef[i]);
            schemaArray[i] = sqlQuery.getTableInsertQuery();
            tableNameArray[i] = sqlQuery.getTableName();
            createIndexArray[i] = sqlQuery.getCreateIndexQuery();

            for (String indexName : sqlQuery.getIndexNames()) {
                indexNameArray.add(indexName);
            }
        }

        SQLOpen sqlOpen = new SQLOpen(
                name,
                version,
                schemaArray,
                tableNameArray,
                createIndexArray,
                indexNameArray,
                context
        );

        return new SQLProvider(sqlOpen.getDatabase(), resolver);
    }
}