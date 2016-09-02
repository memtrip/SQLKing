package com.memtrip.sqlking.preprocessor.processor.freemarker.method;

import com.memtrip.sqlking.preprocessor.processor.data.Column;
import com.memtrip.sqlking.preprocessor.processor.data.Table;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinReferencesMethod implements TemplateMethodModelEx {

    private static final String JOIN = "joinReferences";

    public static Map<String, Object> getMethodMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(JOIN, new JoinReferencesMethod());
        return map;
    }

    private JoinReferencesMethod() {

    }

    private String build(String joinTableName, List<Table> tables) {
        StringBuilder sb = new StringBuilder();

        Table joinTable = getTableFromName(joinTableName, tables);

        if (joinTable != null) {
            List<Column> columns = joinTable.getColumns();
            for (Column column : columns) {
                if (column.isJoinable(tables)) {
                    System.out.println("++ " + column.getName());

                    Table columnTable = column.getRootTable(tables);
                    sb.append(buildJoinTable(joinTableName, columnTable));
                    sb.append(build(column.getClassName(), tables));
                }
            }
        }

        return sb.toString();
    }

    private Table getTableFromName(String tableName, List<Table> tables) {
        for (Table table : tables) {
            if (table.getName().toLowerCase().equals(tableName.toLowerCase())) {
                return table;
            }
        }

        return null;
    }

    private String buildJoinTable(String joinTableName, Table table) {
        return table.getPackage() +
                "." +
                table.getName() +
                " " +
                table.getName().toLowerCase() +
                " = new " +
                table.getPackage() +
                "." +
                table.getName() +
                "();" +
                System.getProperty("line.separator") +
                joinTableName.toLowerCase() +
                ".set" +
                table.getName() +
                "(" +
                table.getName().toLowerCase() +
                ");" +
                System.getProperty("line.separator") +
                System.getProperty("line.separator");
        }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object joinTableNameValue = arguments.get(0);
        Object tablesValue = arguments.get(1);

        String joinTableName = joinTableNameValue instanceof SimpleScalar ?
                joinTableNameValue.toString() :
                String.valueOf(joinTableNameValue);

        List<Table> tables = Util.getTables(tablesValue);
        
        return build(joinTableName, tables);
    }
}