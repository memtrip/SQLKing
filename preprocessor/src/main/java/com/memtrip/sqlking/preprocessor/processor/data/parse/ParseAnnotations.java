package com.memtrip.sqlking.preprocessor.processor.data.parse;

import com.memtrip.sqlking.preprocessor.processor.data.Data;
import com.memtrip.sqlking.preprocessor.processor.data.Table;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.memtrip.sqlking.preprocessor.processor.data.parse.ParseTableAnnotation.parseTable;

public class ParseAnnotations {

    public static Data parse(Set<? extends Element> elements) {
        Data data = new Data();
        data.setTables(assembleTables(elements));
        return data;
    }

    private static List<Table> assembleTables(Set<? extends Element> elements) {
        List<Table> tables = new ArrayList<>();

        for (Element element : elements) {
            if (element.getKind().isClass()) {
                tables.add(parseTable(element));
            }
        }

        return tables;
    }
}
