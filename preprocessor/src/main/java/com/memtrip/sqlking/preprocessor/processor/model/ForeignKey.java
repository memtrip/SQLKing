package com.memtrip.sqlking.preprocessor.processor.model;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

public class ForeignKey {
    private String mPackage;
    private String mThisColumn;
    private String mForeignColumn;
    private String mTable;

    public String getPackage() {
        return mPackage;
    }

    public String getThisColumn() {
        return mThisColumn;
    }

    public String getForeignColumn() {
        return mForeignColumn;
    }

    public String getTable() {
        return mTable;
    }

    public ForeignKey(Element element) {
        mPackage = assemblePackage(element);
        mThisColumn = assembleThisColumn(element);
        mForeignColumn = assembleForeignColumn(element);
        mTable = assembleTable(mPackage);
    }

    private String assemblePackage(Element element) {
        TypeMirror typeMirror = element.asType();
        return typeMirror.toString();
    }

    private String assembleThisColumn(Element element) {
        com.memtrip.sqlking.common.Column member = element.getAnnotation(com.memtrip.sqlking.common.Column.class);
        return member.foreign_key().thisColumn();
    }

    private String assembleForeignColumn(Element element) {
        com.memtrip.sqlking.common.Column member = element.getAnnotation(com.memtrip.sqlking.common.Column.class);
        return member.foreign_key().foreignColumn();
    }

    private String assembleTable(String packageValue) {
        String[] parts = packageValue.split("\\.");
        return parts[parts.length - 1];
    }
}