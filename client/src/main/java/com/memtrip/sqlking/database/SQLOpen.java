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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
class SQLOpen extends SQLiteOpenHelper {
	private SQLiteDatabase mDatabase;
	private String[] mSchemaArray;
	private String[] mTableNameArray;
    private String[] mCreateIndexQuery;
    private List<String> mIndexNames;
	
	SQLiteDatabase getDatabase() {
	    return mDatabase;
	}
	
	SQLOpen(String name, int version, String[] schemaArray,
					  String[] tableNameArray,
					  String[] indexQuery,
					  List<String> indexNames,
					  Context context) {

		super(context, name, null, version);

		mSchemaArray = schemaArray;
		mTableNameArray = tableNameArray;
        mCreateIndexQuery = indexQuery;
        mIndexNames = indexNames;
		mDatabase = getWritableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		for (String schema : mSchemaArray) {
			db.execSQL(schema);
		}

        for (String createIndex : mCreateIndexQuery) {
            if (createIndex != null) {
                db.execSQL(createIndex);
            }
        }
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			destroySchema(db);
			onCreate(db);
		}
	}
	
	/**
	 * Destroy the database schema
	 */
	private void destroySchema(SQLiteDatabase db) {
		for (String tableName : mTableNameArray) {
			db.execSQL("DROP TABLE IF EXISTS " + tableName);
		}

        for (String index : mIndexNames) {
            db.execSQL("DROP INDEX IF EXISTS " + index);
        }
	}
}
