package com.memtrip.sqlking.preprocessor.processor.templates.method;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCursorGetterMethod implements TemplateMethodModelEx {

    private static final String GET_COLUMN_NAMES = "getCursorGetter";

    public static Map<String, Object> getMethodMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(GET_COLUMN_NAMES, new GetCursorGetterMethod());
        return map;
    }

    protected GetCursorGetterMethod() {

    }

    private String assembleTypeGetter(String value) {
        switch (value) {
            case "java.lang.String":
                return "cursor.getString(x)";
            case "long":
                return "cursor.getLong(x)";
            case "int":
                return "cursor.getInt(x)";
            case "boolean":
                return "cursor.getInt(x) == 1 ? true : false";
            case "double":
                return "cursor.getDouble(x)";
            case "byte[]":
                return "cursor.getBlob(x)";
            default:
                return ""; // TODO: foreign key object
        }
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object value = arguments.get(0);

        String typeValue = value instanceof SimpleScalar ?
                value.toString() :
                String.valueOf(value);

        return assembleTypeGetter(typeValue);
    }
}