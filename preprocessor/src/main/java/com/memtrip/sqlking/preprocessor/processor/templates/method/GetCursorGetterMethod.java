package com.memtrip.sqlking.preprocessor.processor.templates.method;

import com.memtrip.sqlking.preprocessor.processor.utils.StringUtils;
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

    private GetCursorGetterMethod() {

    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object value = arguments.get(0);

        String typeValue = value instanceof SimpleScalar ?
                value.toString() :
                String.valueOf(value);

        return StringUtils.assembleTypeGetter(typeValue);
    }
}