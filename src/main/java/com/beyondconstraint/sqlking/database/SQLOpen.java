/**
 * Copyright 2013-present beyond constraint.
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
package com.beyondconstraint.sqlking.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.beyondconstraint.sqlking.Model;
import com.beyondconstraint.sqlking.utils.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author	samkirton
 */
public class SQLOpen extends SQLiteOpenHelper {
	private SQLiteDatabase mDatabase;
	private String[] mSchemaArray;
	private String[] mTableNameArray;
	private Model[] mModel;
	
	protected SQLiteDatabase getDatabase() {
	    return mDatabase;
	}
	
	protected SQLOpen(Context context, String name, int version, String[] schemaArray, String[] tableNameArray, Model[] model) {
		super(context, name, null, version);
		mSchemaArray = schemaArray;
		mTableNameArray = tableNameArray;
		mModel = model;
		mDatabase = getWritableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < mModel.length; i++)
			validateModel(mModel[i].getClass());
		
		for (String schema : mSchemaArray) {
			db.execSQL(schema);
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
	}

	/**
	 * Validate the BaseModel to ensure that the properties match the
	 * get and set methods provided.
	 * @param	classDef	The model to validate
	 */
	private void validateModel(Class<? extends Model> classDef) {
		Field[] fields = classDef.getDeclaredFields();
		String[] methodNames = ReflectionUtils.getMethodNamesFromMethods(classDef.getDeclaredMethods());

		for (int i = 0; i < fields.length; i++) {
			int fieldNameFoundCount = 0;
			String fieldName = fields[i].getName();

			for (int x = 0; x < methodNames.length; x++) {
				if (fieldName.equals(methodNames[x])) {
					fieldNameFoundCount++;

					if (fieldNameFoundCount == 2)
						break;
				}
			}

			if (fieldNameFoundCount != 2)
				throw new IllegalStateException(
						"\"" + fieldName + "\"->[" + classDef.getSimpleName() + "] " +
								"does not have an associated get or set method"
				);
		}
	}
}
