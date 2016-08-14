package com.memtrip.sqlking.preprocessor.processor.data;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private Element mElement;
    private String mName;
    private String mPackage;
    private String mType;
    private List<Column> mColumns;
    private List<ForeignKey> mForeignKeys;

    public Element getElement() {
        return mElement;
    }

    public void setElement(Element newVal) {
        mElement = newVal;
    }

    public String getName() {
        return mName;
    }

    public void setName(String newVal) {
        mName = newVal;
    }

    public String getPackage() {
        return mPackage;
    }

    public void setPackage(String newVal) {
        mPackage = newVal;
    }

    /**
     * (Used in Q.java freemarker template)
     */
    public String getType() {
        return mType;
    }

    public void setType(String newVal) {
        mType = newVal;
    }

    public List<Column> getColumns() {
        return mColumns;
    }

    public void setColumns(List<Column> newVal) {
        mColumns = newVal;
    }

    public List<ForeignKey> getForeignKeys() {
        return mForeignKeys;
    }

    public void setForeignKeys(List<ForeignKey> newVal) {
        mForeignKeys = newVal;
    }

    /**
     * (Used in Q.java freemarker template)
     * @return  all columns ignoring any object mappings
     */
    public List<Column> getPrimitiveColumns(List<Table> tables) {
        List<Column> withoutObjects = new ArrayList<>();

        for (Column column : mColumns) {
            if (!column.isJoinable(tables)) {
                withoutObjects.add(column);
            }
        }

        return withoutObjects;
    }
}
