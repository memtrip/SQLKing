package com.memtrip.sqlking.preprocessor.processor.templates.method;

import com.memtrip.sqlking.preprocessor.processor.model.Table;
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModelException;

import java.util.ArrayList;
import java.util.List;

public class TransformModelUtil {

    public static List<Table> getTables(Object simpleSequenceValue) throws TemplateModelException {
        List<Table> tables = new ArrayList<>();

        if (simpleSequenceValue instanceof SimpleSequence) {
            SimpleSequence simpleSequence = (SimpleSequence) simpleSequenceValue;

            for (int i = 0; i < simpleSequence.size(); i++) {
                StringModel templateModel = (StringModel) simpleSequence.get(i);
                Table table = (Table) templateModel.getAdaptedObject(Table.class);
                tables.add(table);
            }
        }

        return tables;
    }
}
