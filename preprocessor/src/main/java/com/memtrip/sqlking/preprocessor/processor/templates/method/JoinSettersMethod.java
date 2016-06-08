package com.memtrip.sqlking.preprocessor.processor.templates.method;

import com.memtrip.sqlking.preprocessor.processor.column.Column;
import com.memtrip.sqlking.preprocessor.processor.model.Table;
import com.memtrip.sqlking.preprocessor.processor.utils.StringUtils;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinSettersMethod implements TemplateMethodModelEx {

    private static final String JOIN = "join";

    public static Map<String, Object> getMethodMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(JOIN, new JoinSettersMethod());
        return map;
    }

    private JoinSettersMethod() {

    }

    private String build(String joinTableName, List<Table> tables) {
        StringBuilder sb = new StringBuilder();

        for (Table table : tables) {
            if (table.getName().toLowerCase().equals(joinTableName.toLowerCase())) {
                List<Column> columns = table.getColumns();
                for (Column column : columns) {
                    if (column.isJoinable(tables)) {
                        sb.append(build(column.getClassName(), tables));
                    } else {
                        sb.append("} else if (cursor.getColumnName(x).equals(\"")
                                .append(table.getName())
                                .append("_")
                                .append(column.getName())
                                .append("\")) {")
                                .append(System.getProperty("line.separator"))
                                .append(table.getName().toLowerCase())
                                .append(".set")
                                .append(StringUtils.firstToUpperCase(column.getName()))
                                .append("(")
                                .append(StringUtils.assembleTypeGetter(column.getType()))
                                .append(");");
                    }

                    sb.append(System.getProperty("line.separator"));
                }

                break;
            }
        }

        return sb.toString();
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object joinTableNameValue = arguments.get(0);
        Object tablesValue = arguments.get(1);

        String joinTableName = joinTableNameValue instanceof SimpleScalar ?
                joinTableNameValue.toString() :
                String.valueOf(joinTableNameValue);

        List<Table> tables = TransformModelUtil.getTables(tablesValue);

        String join = build(joinTableName, tables);
        if (join.length() > 0) {
            // remove the trailing "}"
            join = join.substring(0,join.length()-1);
        }

        return join;
    }
}