package com.memtrip.sqlking.preprocessor.processor.model;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

public class ForeignKey {
    private String mPackage;
    private String mColumn;

    public String getPackage() {
        return mPackage;
    }

    public String getColumn() {
        return mColumn;
    }

    public ForeignKey(Element element) {
        mPackage = assemblePackage(element);
        mColumn = assembleColumn(element);
        System.out.println();
    }

    private String assemblePackage(Element element) {
        TypeMirror typeMirror = element.asType();
        return typeMirror.toString();
    }

    private String assembleColumn(Element element) {
        com.memtrip.sqlking.common.Column member = element.getAnnotation(com.memtrip.sqlking.common.Column.class);
        return member.foreign_key();
    }
}
