package com.memtrip.sqlking.preprocessor.processor.data.validator;

import com.memtrip.sqlking.preprocessor.processor.Validator;
import com.memtrip.sqlking.preprocessor.processor.ValidatorException;
import com.memtrip.sqlking.preprocessor.processor.data.Column;
import com.memtrip.sqlking.preprocessor.processor.data.Data;
import com.memtrip.sqlking.preprocessor.processor.data.Table;

import java.util.List;

public class PrimaryKeyMustBeUnique implements Validator {

    private Data data;

    public PrimaryKeyMustBeUnique(Data data) {
        this.data = data;
    }

    private boolean primaryKeyIsUniqueInColumns(List<Column> columns) {
        int occurrences = 0;

        if (columns != null) {
            for (Column column : columns) {
                if (column.hasPrimaryKey()) {
                    occurrences++;
                }
            }
        }

        return occurrences > 1;
    }

    @Override
    public void validate() throws ValidatorException {
        for (Table table : data.getTables()) {
            if (primaryKeyIsUniqueInColumns(table.getColumns())) {
                throw new ValidatorException(
                        table.getElement(),
                        "[Duplicate primary_key's found in @Table: `" + table.getName()
                                + ", only specify one primary_key Column per table]"
                );
            }
        }
    }
}