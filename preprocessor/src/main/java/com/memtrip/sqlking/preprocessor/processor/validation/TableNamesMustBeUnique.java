package com.memtrip.sqlking.preprocessor.processor.validation;

import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.model.Table;

import java.util.List;

public class TableNamesMustBeUnique implements Validator {
    private Data mData;

    public TableNamesMustBeUnique(Data data) {
        mData = data;
    }

    private Table findDuplicateTable(List<Table> tables) {
        for (Table table : tables) {
            Table duplicateTable = getDuplicateTable(table, tables);
            if (duplicateTable != null) {
                return duplicateTable;
            }
        }

        return null;
    }

    private Table getDuplicateTable(Table check, List<Table> tables) {
        int occurrences = 0;

        for (Table table : tables) {
            if (check.getName().equals(table.getName())) occurrences++;
            if (occurrences > 1) return table;
        }

        return null;
    }

    @Override
    public void validate() throws ValidatorException {
        Table table = findDuplicateTable(mData.getTables());
        if (table != null) {
            throw new ValidatorException(table.getElement(), "[The @Table: `" + table.getName() + "` is duplicated, table names must be unique]");
        }
    }
}
