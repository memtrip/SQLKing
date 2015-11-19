package com.memtrip.sqlking.preprocessor.processor.model;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Data {
    private List<Table> mTables;

    public List<Table> getTables() {
        return mTables;
    }

    public Data(Set<? extends Element> elements) {
        mTables = assembleTables(elements);
    }

    private List<Table> assembleTables(Set<? extends Element> elements) {
        List<Table> tables = new ArrayList<>();

        for (Element element : elements) {
            if (element.getKind().isClass()) {
                tables.add(new Table(element));
            }
        }

        return tables;
    }
}