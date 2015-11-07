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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.beyondconstraint.sqlking.Model;
import com.beyondconstraint.sqlking.operation.clause.And;
import com.beyondconstraint.sqlking.operation.clause.Or;
import com.beyondconstraint.sqlking.utils.ReflectionUtils;
import com.beyondconstraint.sqlking.utils.StringUtils;
import com.beyondconstraint.sqlking.operation.clause.Clause;
import com.beyondconstraint.sqlking.operation.clause.In;
import com.beyondconstraint.sqlking.operation.keyword.Limit;
import com.beyondconstraint.sqlking.operation.keyword.OrderBy;
import com.beyondconstraint.sqlking.operation.clause.Where;
import com.beyondconstraint.sqlking.schema.Column;
import com.beyondconstraint.sqlking.schema.PrimitiveType;
import com.beyondconstraint.sqlking.schema.DBEngineType;

/**
 * A SQLite database helper class that handles the creation and updating of 
 * database tables. SQL statements that create tables should be defined here,
 * they should then be executed from the onCreate method. Drop queries should also
 * be provided for the database update.
 * @author	samkirton
 */
public class DatabaseController {

    protected DatabaseController() { }

    /**
     * The table name is determined by the model class name
     * @param classDef
     * @return
     */
	public String getTableName(Class<?> classDef) {
		return classDef.getSimpleName();
	}

    /**
     * The table name is determined by the model class name
     * @param model
     * @return
     */
    public String getTableName(Model model) {
        return getTableName(model.getClass());
    }

    /**
     * Build the conditional clause
     * @param clause
     * @return
     */
    public String getClause(Clause[] clause) {
        StringBuilder clauseBuilder = new StringBuilder();

        if (clause != null) {
            for (Clause item : clause) {
                clauseBuilder.append(getClause(item));
            }
        }

        return clauseBuilder.toString();
    }

    /**
     * Loop through the supported Clause implementations and build the clause accordingly
     * @param clause
     * @return
     */
    private String getClause(Clause clause) {
        StringBuilder clauseBuilder = new StringBuilder();

        if (clause instanceof In) {
            clauseBuilder.append(buildInCondition((In) clause));
        } else if (clause instanceof Where) {
            clauseBuilder.append(buildWhereCondition((Where) clause));
        } else if (clause instanceof And) {
            clauseBuilder.append(StringUtils.BRACKET_START);
            And and = (And)clause;
            for (Clause item : and.getClause()) {
                clauseBuilder.append(getClause(item));
                clauseBuilder.append(StringUtils.SPACE);
                clauseBuilder.append("AND");
                clauseBuilder.append(StringUtils.SPACE);
            }

            // remove the excess AND with its 2 spaces
            clauseBuilder.delete(clauseBuilder.length() - 5, clauseBuilder.length());
            clauseBuilder.append(StringUtils.BRACKET_END);
        } else if (clause instanceof Or) {
            clauseBuilder.append(StringUtils.BRACKET_START);
            Or or = (Or)clause;
            for (Clause item : or.getClause()) {
                clauseBuilder.append(getClause(item));
                clauseBuilder.append(StringUtils.SPACE);
                clauseBuilder.append(("OR"));
                clauseBuilder.append(StringUtils.SPACE);
            }

            // remove the excess OR with its 2 spaces
            clauseBuilder.delete(clauseBuilder.length() - 4, clauseBuilder.length());
            clauseBuilder.append(StringUtils.BRACKET_END);
        }

        return clauseBuilder.toString();
    }

    /**
     * Build the WHERE conditional string
     * @param condition
     * @return
     */
    private String buildWhereCondition(Where condition) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(condition.getRow());
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(condition.getExpression().toString());
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(StringUtils.VALUE);

        return stringBuilder.toString();
    }

    /**
     * Build the IN condition string
     * @param in
     * @return
     */
    private String buildInCondition(In in) {
        return StringUtils.buildInClause(in.getRow(), in.getValues().length);
    }

    /**
     * Build the clause arguments
     * @param clause
     * @return
     */
    public String[] getClauseArgs(Clause[] clause) {
        List<String> args = new ArrayList<>();

        if (clause != null) {
            for (Clause item : clause) {
                args.addAll(getClauseArgs(item));
            }
        }

        return args.toArray(new String[args.size()]);
    }

    /**
     * Loop through the supported Clause implementations and build the args accordingly
     * @param clause
     * @return
     */
    private List<String> getClauseArgs(Clause clause) {
        List<String> args = new ArrayList<>();

        if (clause instanceof In) {
            args.addAll(buildInArgs((In) clause));
        } else if (clause instanceof Where) {
            args.add(buildWhereArgs((Where) clause));
        } else if (clause instanceof And)  {
            And and = (And)clause;
            for (Clause item : and.getClause()) {
                args.addAll(getClauseArgs(item));
            }
        } else if (clause instanceof Or) {
            Or or = (Or)clause;
            for (Clause item : or.getClause()) {
                args.addAll(getClauseArgs(item));
            }
        }

        return args;
    }

    /**
     * Build the WHERE arguments
     * @param where
     * @return
     */
    private String buildWhereArgs(Where where) {
        return StringUtils.getArgValue(where.getValue());
    }

    /**
     * Build the IN arguments
     * @param in
     * @return
     */
    private List<String> buildInArgs(In in) {
        List<String> args = new ArrayList<>();

        for (int i = 0; i < in.getValues().length; i++) {
            args.add(String.valueOf(in.getValues()[i]));
        }

        return args;
    }

    /**
     * Build the order by keyword part of the SQL statement
     * @param orderBy
     * @return
     */
    public String getOrderBy(OrderBy orderBy) {
        StringBuilder stringBuilder = new StringBuilder();

        if (orderBy != null) {
            stringBuilder.append(orderBy.getField());
            stringBuilder.append(StringUtils.SPACE);
            stringBuilder.append(orderBy.getOrder().toString());
        }

        return stringBuilder.toString();
    }

    /**
     * Build the limit keyword part of the SQL statement
     * @param limit
     * @return
     */
    public String getLimit(Limit limit) {
        StringBuilder stringBuilder = new StringBuilder();

        if (limit != null) {
            stringBuilder.append(limit.getStart());
            stringBuilder.append(StringUtils.COMMA);
            stringBuilder.append(limit.getEnd());
        }

        return stringBuilder.toString();
    }

    public InsertQuery createInsertQuery(Model model) {
        return new InsertQuery(
            getTableName(model),
            getContentValuesFromModel(model)
        );
    }

    /**
     * Retrieve the column names for the provided BaseModel
     * @param	classDef	The classDef of the model to retrieve the column name for
     * @return	An array of column names associated with the provided BaseModel
     */
    public String[] getSQLColumnNamesFromModel(Class<?> classDef) {
        Field[] fields = classDef.getDeclaredFields();
        String[] columns = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            columns[i] = field.getName();
        }

        return columns;
    }

    /**
     * Populate a ContentValues collection based on the field values stored in baseModel
     * @param	model	The baseModel to get the ContentValues from
     * @return	The ContentValues object populated from the provided baseModel
     */
    public ContentValues getContentValuesFromModel(Model model) {
        Field[] fields = model.getClass().getDeclaredFields();
        HashMap<String,Method> getMethods = ReflectionUtils.getGetterMethods(model.getClass());
        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            String type = getSQLDataTypeFromClassRef(field.getType());

            if (type.equals(DBEngineType.SQL_TEXT)) {
                contentValues.put(name, (String) ReflectionUtils.invokeMethod(model, getMethods.get(name)));
            } else if (type.equals(DBEngineType.SQL_INTEGER)) {
                contentValues.put(name, ((Integer) ReflectionUtils.invokeMethod(model, getMethods.get(name))).intValue());
            } else if (type.equals(DBEngineType.SQL_BOOLEAN)) {
                boolean value = ((Boolean) ReflectionUtils.invokeMethod(model, getMethods.get(name))).booleanValue();
                contentValues.put(name, value ? 1 : 0);
            } else if (type.equals(DBEngineType.SQL_LONG)) {
                contentValues.put(name, ((Long) ReflectionUtils.invokeMethod(model, getMethods.get(name))).longValue());
            } else if (type.equals(DBEngineType.SQL_REAL)) {
                contentValues.put(name, ((Double) ReflectionUtils.invokeMethod(model, getMethods.get(name))).doubleValue());
            } else if (type.equals(DBEngineType.SQL_BLOB)) {
                contentValues.put(name, (byte[]) ReflectionUtils.invokeMethod(model, getMethods.get(name)));
            }
        }

        return contentValues;
    }

	/**
	 * A generic method that takes any class interface that extends BaseModel, it takes the provided
	 * cursor and populates a new instance of the provided BaseModel interface with the results.
	 * @param	classDef	The class interface to return the cursor results in
	 * @param	cursor	The provided SQLite cursor that the results should be populated from
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] retrieveSQLSelectResults(Class<?> classDef, Cursor cursor) {
		if (classDef.isAssignableFrom(Model.class))
			throw new IllegalArgumentException("Only classes that implement Model can be populated with cursor results");

		HashMap<String, Integer> columnTypeMap = getColumnNameTypeMap(classDef);
		HashMap<String,Method> setMethods = ReflectionUtils.getSetterMethods(classDef);
		
		T[] result = (T[]) Array.newInstance(classDef, cursor.getCount());
		// Loop through all the rows in the cursor and execute the appropriate 
		// sqlModel setter method, this will populate the model object.
		cursor.moveToFirst();
		for (int i = 0; !cursor.isAfterLast(); i++) {
			T model = (T) ReflectionUtils.newInstance(classDef);
			
			for (int x = 0; x < cursor.getColumnCount(); x++) {
				Method executeMethod = setMethods.get(cursor.getColumnName(x));
				setColumnValue(model, columnTypeMap, executeMethod, cursor, x);
			}
			
			result[i] = model;
			cursor.moveToNext();
		}

        cursor.close();
		
		return result;
	}

    /**
     * Build a HashMap for column name / type key value pairs
     * @param	classDef	The baseModel to get the HashMap from
     * @return	A HashMap that contains column name / type key value pairs
     */
    private HashMap<String,Integer> getColumnNameTypeMap(Class<?> classDef) {
        HashMap<String,Integer> columnNameTypeMap = new HashMap<String,Integer>();
        Field[] fields = classDef.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            columnNameTypeMap.put(field.getName(), getORMDataTypeFromClassRef(field.getType()));
        }

        return columnNameTypeMap;
    }

    /**
     * Determine the data type of the provided class reference and return
     * the associated ORM data type
     * @param	classDef	The class reference
     * @returnn	The ORM data type to return
     */
    private static int getORMDataTypeFromClassRef(Class<?> classDef) {
        int dataType = -1;

        if (classDef.equals(String.class)) {
            dataType = PrimitiveType.FIELD_STRING;
        } else if (classDef.equals(int.class)) {
            dataType = PrimitiveType.FIELD_INTEGER;
        } else if (classDef.equals(boolean.class)) {
            dataType = PrimitiveType.FIELD_BOOLEAN;
        } else if (classDef.equals(long.class)) {
            dataType = PrimitiveType.FIELD_LONG;
        } else if (classDef.equals(double.class)) {
            dataType = PrimitiveType.FIELD_DOUBLE;
        } else if (classDef.equals(byte[].class)) {
            dataType = PrimitiveType.FIELD_BLOB;
        }

        return dataType;
    }

    /**
     * Use reflection to access the setter methods of the baseModel instance, then set
     * the value of whatever type is associated with the current cursorColumn. The method
     * cursor.getType(int) is only available in API 11+, so to determine the type, each table column
     * has a key value pair comprising of columnName/dataType
     * @param	model	The object to invoke the setter method on
     * @param	columnTypeMap	columnName/dataType pairs
     * @param	executeMethod	The setter method that will be invoked on the provided object
     * @param	cursor	The database cursor to get the value from
     * @param	index	The cursor index that the desired value is positioned at
     */
    private void setColumnValue(Object model, HashMap<String,Integer> columnTypeMap, Method executeMethod, Cursor cursor, int index) {
        int columnType = columnTypeMap.get(cursor.getColumnName(index));

        switch (columnType) {
            case PrimitiveType.FIELD_STRING:
                ReflectionUtils.invokeMethod(model, executeMethod, cursor.getString(index));
                break;

            case PrimitiveType.FIELD_INTEGER:
                ReflectionUtils.invokeMethod(model, executeMethod, cursor.getInt(index));
                break;

            case PrimitiveType.FIELD_BOOLEAN:
                boolean value = cursor.getInt(index) == 1 ? true : false;
                ReflectionUtils.invokeMethod(model, executeMethod, value);
                break;

            case PrimitiveType.FIELD_LONG:
                ReflectionUtils.invokeMethod(model, executeMethod, cursor.getLong(index));
                break;

            case PrimitiveType.FIELD_BLOB:
                ReflectionUtils.invokeMethod(model, executeMethod, cursor.getBlob(index));
                break;

            case PrimitiveType.FIELD_DOUBLE:
                ReflectionUtils.invokeMethod(model, executeMethod, cursor.getDouble(index));
                break;
        }
    }

    /**
     * Determine the data type of the provided class reference and return
     * the associated SQL data type
     * @param	classDef	The class reference
     * @return	The SQL data type to return
     */
    public static String getSQLDataTypeFromClassRef(Class<?> classDef) {
        String dataType = null;

        if (classDef.equals(String.class)) {
            dataType = DBEngineType.SQL_TEXT;
        } else if (classDef.equals(int.class)) {
            dataType = DBEngineType.SQL_INTEGER;
        } else if (classDef.equals(boolean.class)) {
            dataType = DBEngineType.SQL_BOOLEAN;
        } else if (classDef.equals(long.class)) {
            dataType = DBEngineType.SQL_LONG;
        } else if (classDef.equals(double.class)) {
            dataType = DBEngineType.SQL_REAL;
        } else if (classDef.equals(byte[].class)) {
            dataType = DBEngineType.SQL_BLOB;
        }

        return dataType;
    }
}
