package com.memtrip.sqlking.preprocessor.processor.data.parse;

import com.memtrip.sqlking.preprocessor.processor.data.Column;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

class ParseColumnAnnotation {

    static Column parseColumn(Element element) {

        String name = assembleName(element);
        boolean isIndex = assembleIsIndex(element);
        String type = assembleType(element);
        String className = assembleClassName(type);

        Column column = new Column();
        column.setName(name);
        column.setIsIndex(isIndex);
        column.setType(type);
        column.setClassName(className);

        return column;
    }

    private static String assembleName(Element element) {
        Name name = element.getSimpleName();
        return name.toString();
    }

    private static String assembleType(Element element) {
        TypeMirror typeMirror = element.asType();
        return typeMirror.toString();
    }

    private static String assembleClassName(String type) {
        String[] packageParts = type.split("\\.");
        return packageParts[packageParts.length - 1];
    }

    private static boolean assembleIsIndex(Element element) {
        com.memtrip.sqlking.common.Column column = element.getAnnotation(com.memtrip.sqlking.common.Column.class);
        return column != null && column.index();
    }
}
