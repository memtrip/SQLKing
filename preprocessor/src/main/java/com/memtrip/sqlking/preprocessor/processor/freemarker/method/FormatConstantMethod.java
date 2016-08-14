package com.memtrip.sqlking.preprocessor.processor.freemarker.method;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatConstantMethod implements TemplateMethodModelEx {

    private static final String FORMAT_CONSTANT = "formatConstant";

    public static Map<String, Object> getMethodMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(FORMAT_CONSTANT, new FormatConstantMethod());
        return map;
    }

    private FormatConstantMethod() {

    }

    private static String formatConstant(String value) {
        StringBuilder sb = new StringBuilder();

        char[] items = value.toCharArray();
        for (int i = 0; i < items.length; i++) {
            char item = items[i];
            if (Character.isUpperCase(item) && (i-1 >= 0 && !String.valueOf(items[i-1]).equals("_"))) {
                sb.append("_");
            }

            sb.append(item);
        }

        return sb.toString().toUpperCase();
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object value = arguments.get(0);

        String typeValue = value instanceof SimpleScalar ?
                value.toString() :
                String.valueOf(value);

        return formatConstant(typeValue);
    }
}
