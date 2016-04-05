package com.memtrip.sqlking.preprocessor.processor.validation;

import com.memtrip.sqlking.preprocessor.processor.model.Column;
import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.model.ForeignKey;
import com.memtrip.sqlking.preprocessor.processor.model.Table;

import java.util.List;

public class ForeignKeyColumnMustBeMemberOfAnnotatedVariableType implements Validator {
    private Data mData;

    public ForeignKeyColumnMustBeMemberOfAnnotatedVariableType(Data data) {
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
                if (!foreignKeyColumnExistsForAnnotatedType(column)) {
                    return column;
                }
            }
        }

        return null;
    }

    private boolean foreignKeyColumnExistsForAnnotatedType(Column member) {
        String column = member.getForeignKey().getColumn();
        String type = getForeignKeyType(member.getForeignKey());
        Table table = getTableByName(mData.getTables(), type);
        return columnExistsInTable(column, table);
    }

    private String getForeignKeyType(ForeignKey foreignKey) {
        String package1 = foreignKey.getPackage();
        String[] packageParts = package1.split("\\.");
        return packageParts[packageParts.length - 1];
    }

    private Table getTableByName(List<Table> tables, String type) {
        for (Table table : tables) {
            if (table.getName().equals(type)) {
                return table;
            }
        }

        return null;
    }

    private boolean columnExistsInTable(String column, Table table) {
        List<Column> columns = table.getColumns();
        for (Column member : columns) {
            if (member.getName().equals(column) || column.equals("_id")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void validate() throws ValidatorException {
        Column column = getTableMemberWithInvalidForeignKey(mData.getTables());
        if (column != null) {
            throw new ValidatorException(column.getElement(), "[@Column foreign_key value must be either `_id` or a column variable of the annotated property]");
        }
    }
}
