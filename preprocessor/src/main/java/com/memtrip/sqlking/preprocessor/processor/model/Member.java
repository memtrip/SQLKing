package com.memtrip.sqlking.preprocessor.processor.model;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

public class Member {
    private Element mElement;
    private String mName;
    private String mType;
    private boolean mIsIndex;
    private ForeignKey mForeignKey;

    public Element getElement() {
        return mElement;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public boolean isIndex() {
        return mIsIndex;
    }

    public ForeignKey getForeignKey() {
        return mForeignKey;
    }

    public boolean hasForeignKey() {
        return mForeignKey != null;
    }

    public Member(Element element) {
        mElement = element;
        mName = assembleName(element);
        mType = assembleType(element);
        mIsIndex = assembleIsIndex(element);
        mForeignKey = assembleForeignKey(element);

        System.out.print("");
    }

    private String assembleName(Element element) {
        Name name = element.getSimpleName();
        return name.toString();
    }

    private String assembleType(Element element) {
        TypeMirror typeMirror = element.asType();
        return typeMirror.toString();
    }

    private boolean assembleIsIndex(Element element) {
        com.memtrip.sqlking.common.Member member = element.getAnnotation(com.memtrip.sqlking.common.Member.class);
        return member.index();
    }

    private ForeignKey assembleForeignKey(Element element) {
        com.memtrip.sqlking.common.Member member = element.getAnnotation(com.memtrip.sqlking.common.Member.class);
        String foreignKey = member.foreign_key();
        return (!foreignKey.equals("")) ? new ForeignKey(element) : null;
    }
}