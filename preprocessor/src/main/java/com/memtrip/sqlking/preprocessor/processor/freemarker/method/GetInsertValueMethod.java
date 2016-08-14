package com.memtrip.sqlking.preprocessor.processor.freemarker.method;

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

    private String assembleInsertValue(String value, String getter) {
        switch (value) {
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
                return ""; // TODO: foreign key object
        }
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object value = arguments.get(0);
        Object getter = arguments.get(1);

        String typeValue = value instanceof SimpleScalar ?
                value.toString() :
                String.valueOf(value);

        String getterValue = getter instanceof SimpleScalar ?
                getter.toString() :
                String.valueOf(getter);

        return assembleInsertValue(typeValue, getterValue);
    }
}
