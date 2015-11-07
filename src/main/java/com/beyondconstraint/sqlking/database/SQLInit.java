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

import com.beyondconstraint.sqlking.Model;
import com.beyondconstraint.sqlking.utils.ReflectionUtils;
import com.beyondconstraint.sqlking.utils.StringUtils;
import com.beyondconstraint.sqlking.schema.Column;

import java.lang.reflect.Field;

/**
 * Build the SQL database based on the provided models
 * @author samkirton
 */
public class SQLInit {
	private SQLDatabase mDatabase;
	
	public SQLDatabase getDatabase() {
	    return mDatabase;
	}
	
	public SQLInit(String name, int version, Model[] models, Context context) {
		String[] schemaArray = new String[models.length];
		String[] tableNameArray = new String[models.length];
		for (int i = 0; i < models.length; i++) {
			Model baseModel = models[i];
			schemaArray[i] = buildSchemaFromBaseModel(baseModel);
			tableNameArray[i] = baseModel.getClass().getSimpleName();
		}
		
		SQLOpen SQLOpen = new SQLOpen(context, name, version, schemaArray, tableNameArray, models);
		mDatabase = new SQLDatabase(SQLOpen.getDatabase());
	}
	
	/**
	 * Build a schema from the provided model
	 * @param	model	The baseModel to build the schema from
	 * @return	A schema string built on the provided baseModel
	 */
	private String buildSchemaFromBaseModel(Model model) {
		String tableName = ReflectionUtils.getClassName(model);
		Column[] columns = getSQLColumnFromModel(model.getClass());
		return StringUtils.buildCreateTableStatement(tableName, columns);
	}

	/**
	 * Retrieve the column information for the provided model
	 * @param	classDef	The classDef of the model to retrieve the column information for
	 * @return	An array of column information associated with the provided BaseModel
	 */
	private Column[] getSQLColumnFromModel(Class<? extends Model> classDef) {
		Field[] fields = classDef.getDeclaredFields();
		Column[] columns = new Column[fields.length];

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Column column = new Column();
			column.setName(field.getName());
			column.setType(DatabaseController.getSQLDataTypeFromClassRef(field.getType()));
			columns[i] = column;
		}

		return columns;
	}
}