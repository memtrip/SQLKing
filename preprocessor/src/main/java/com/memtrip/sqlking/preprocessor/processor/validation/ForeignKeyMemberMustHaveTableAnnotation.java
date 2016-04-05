package com.memtrip.sqlking.preprocessor.processor.validation;

import com.memtrip.sqlking.preprocessor.processor.model.Column;
import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.model.ForeignKey;
import com.memtrip.sqlking.preprocessor.processor.model.Table;

import java.util.List;

public class ForeignKeyMemberMustHaveTableAnnotation implements Validator {
    private Data mData;

    public ForeignKeyMemberMustHaveTableAnnotation(Data data) {
        mData = data;
    }

    private Column getTableMemberWithInvalidForeignKey(List<Table> tables) {
        Column column = null;

        for (Table table : tables) {
            column = getMemberWithInvalidForeignKey(table.getColumns());
        }

        return column;
    }

    private Column getMemberWithInvalidForeignKey(List<Column> columns) {

        for (Column column : columns) {
            if (column.getForeignKey() != null) {
                ForeignKey foreignKey = column.getForeignKey();
                if (!foreignKeyIsAnnotatingTable(foreignKey, mData.getTables())) {
                    return column;
                }
            }
        }

        return null;
    }

    private boolean foreignKeyIsAnnotatingTable(ForeignKey foreignKey, List<Table> tables) {
        for (Table table : tables) {
            String foreignKeyType = getForeignKeyType(foreignKey);
            if (table.getName().toLowerCase().equals(foreignKeyType.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private String getForeignKeyType(ForeignKey foreignKey) {
        String package1 = foreignKey.getPackage();
        String[] packageParts = package1.split("\\.");
        return packageParts[packageParts.length - 1];
    }

    @Override
    public void validate() throws ValidatorException {
        Column column = getTableMemberWithInvalidForeignKey(mData.getTables());
        if (column != null) {
            throw new ValidatorException(column.getElement(), "[A @Column with a foreign_key can only annotate a variable whose data type is annotated with @Table]");
        }
    }
}
