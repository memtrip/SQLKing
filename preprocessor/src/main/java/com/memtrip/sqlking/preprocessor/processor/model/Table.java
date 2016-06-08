package com.memtrip.sqlking.preprocessor.processor.model;

import com.memtrip.sqlking.preprocessor.processor.Context;
import com.memtrip.sqlking.preprocessor.processor.column.Column;
import com.memtrip.sqlking.preprocessor.processor.column.ParseColumnAnnotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private Element mElement;
    private String mName;
    private String mPackage;
    private List<Column> mColumns;

    public Element getElement() {
        return mElement;
    }

    public String getName() {
        return mName;
    }

    public String getPackage() {
        return mPackage;
    }

    public String getType() {
        return mPackage + "." + mName;
    }

    public List<Column> getColumns() {
        return mColumns;
    }

    /**
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

    public Table(Element element) {
        mElement = element;
        mName = assembleName(element);
        mPackage = assemblePackage(element);
        mColumns = assembleMembers(element);
    }

    private String assembleName(Element element) {
        Name name = element.getSimpleName();
        return name.toString();
    }

    private String assemblePackage(Element element) {
        PackageElement packageElement = Context.getInstance().getElementUtils().getPackageOf(element);
        Name name = packageElement.getQualifiedName();
        return name.toString();
    }

    private List<Column> assembleMembers(Element element) {
        List<Column> columns = new ArrayList<>();

        if (element.getEnclosedElements() != null && element.getEnclosedElements().size() > 0) {
            for (Element childElement : element.getEnclosedElements()) {
                if (childElement.getKind().isField() && childElement.getAnnotation(com.memtrip.sqlking.common.Column.class) != null) {
                    columns.add(ParseColumnAnnotation.parseColumn(childElement));
                }
            }
        }

        return columns;
    }
}
