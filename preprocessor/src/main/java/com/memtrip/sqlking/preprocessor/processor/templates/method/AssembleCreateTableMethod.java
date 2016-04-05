package com.memtrip.sqlking.preprocessor.processor.templates.method;

import com.memtrip.sqlking.preprocessor.processor.model.Column;
import com.memtrip.sqlking.preprocessor.processor.model.Table;
import freemarker.ext.beans.StringModel;
import freemarker.template.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssembleCreateTableMethod implements TemplateMethodModelEx {

    private static final String ASSEMBLE_CREATE_TABLE = "assembleCreateTable";

    public static final String SQL_TEXT = "text";
    public static final String SQL_INTEGER = "integer";
    public static final String SQL_LONG = "long";
    public static final String SQL_REAL = "real";
    public static final String SQL_BLOB = "blob";

    public static Map<String, Object> getMethodMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ASSEMBLE_CREATE_TABLE, new AssembleCreateTableMethod());
        return map;
    }

    protected AssembleCreateTableMethod() {

    }

    /**
     * Build a create table statement based on the provided tableName and members
     * @param	table	The table that the statement will create
     * @return	A SQL statement that will create a table
     */
    public String buildCreateTableStatement(Table table) {
        StringBuilder statementBuilder = new StringBuilder();

        statementBuilder.append("CREATE TABLE ");
        statementBuilder.append(table.getName());
        statementBuilder.append(" (");

        for (int i = 0; i < table.getColumns().size(); i++) {
            Column column = table.getColumns().get(i);
            statementBuilder.append(column.getName());
            statementBuilder.append(" ");
            statementBuilder.append(getSQLDataTypeFromClassRef(column.getType()));

            // do not display the comma on the last column entry
            if (i != table.getColumns().size()-1)
                statementBuilder.append(",");
        }

        statementBuilder.append(");");

        return "\"" + statementBuilder.toString() + "\";";
    }

    /**
     * Determine the data type of the provided class reference and return
     * the associated SQL data type
     * @param	value	The class reference
     * @return	The SQL data type to return
     */
    private String getSQLDataTypeFromClassRef(String value) {
        switch (value) {
            case "java.lang.String":
                return SQL_TEXT;
            case "long":
                return SQL_LONG;
            case "int":
                return SQL_INTEGER;
            case "boolean":
                return SQL_INTEGER;
            case "double":
                return SQL_REAL;
            case "byte[]":
                return SQL_BLOB;
            default:
                return ""; // TODO: foreign key object
        }
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.get(0) instanceof StringModel) {
            StringModel stringModel = (StringModel)arguments.get(0);
            Table table = (Table)stringModel.getAdaptedObject(Table.class);
            return buildCreateTableStatement(table);
        } else {
            throw new IllegalStateException("The assembleCreateTable argument must be type of com.memtrip.sqlking.preprocessor.model.Table");
        }
    }
}