package com.memtrip.sqlking.preprocessor.processor.freemarker.method;

import com.memtrip.sqlking.preprocessor.processor.data.Column;
import com.memtrip.sqlking.preprocessor.processor.data.Table;
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetInsertValueMethod implements TemplateMethodModelEx {

    private static final String GET_INSERT_VALUE = "getInsertValue";

    public static Map<String, Object> getMethodMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(GET_INSERT_VALUE, new GetInsertValueMethod());
        return map;
    }


    private GetInsertValueMethod() {

    }

    private String assembleInsertValue(Column column, String getter) {
        if (column.hasPrimaryKey() && column.hasAutoIncrement()) {
            return "NULL";
        } else {
            switch (column.getType()) {
                case "java.lang.String":
                case "long":
                case "int":
                case "double":
                    return "'\" + " + getter + " + \"'";
                case "boolean":
                    return "\" + (" + getter + " ? \"'1'\" : \"'0'\") + \"";
                case "byte[]":
                    return "\" + assembleBlob(" + getter + ") + \"";
                default:
                    return "";
            }
        }
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object columnValue = arguments.get(0);
        Object getter = arguments.get(1);

        Column column;
        if (columnValue instanceof StringModel) {
            StringModel stringModel = (StringModel)columnValue;
            column = (Column)stringModel.getAdaptedObject(Column.class);
        } else {
            throw new IllegalStateException("The getInsertValue argument must be type of " +
                    "com.memtrip.sqlking.preprocessor.processor.data.Column");
        }

        String getterValue = getter instanceof SimpleScalar ?
                getter.toString() :
                String.valueOf(getter);

        return assembleInsertValue(column, getterValue);
    }
}
