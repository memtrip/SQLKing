package com.memtrip.sqlking.preprocessor.processor.validation;

import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.model.ForeignKey;
import com.memtrip.sqlking.preprocessor.processor.model.Member;
import com.memtrip.sqlking.preprocessor.processor.model.Table;

import java.util.List;

public class ForeignKeyColumnMustBeMemberOfAnnotatedVariableType implements Validator {
    private Data mData;

    public ForeignKeyColumnMustBeMemberOfAnnotatedVariableType(Data data) {
        mData = data;
    }

    private Member getTableMemberWithInvalidForeignKey(List<Table> tables) {
        Member member = null;

        for (Table table : tables) {
            member = getMemberWithInvalidForeignKey(table.getMembers());
        }

        return member;
    }

    private Member getMemberWithInvalidForeignKey(List<Member> members) {

        for (Member member : members) {
            if (member.getForeignKey() != null) {
                if (!foreignKeyColumnExistsForAnnotatedType(member)) {
                    return member;
                }
            }
        }

        return null;
    }

    private boolean foreignKeyColumnExistsForAnnotatedType(Member member) {
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
        List<Member> members = table.getMembers();
        for (Member member : members) {
            if (member.getName().equals(column) || column.equals("_id")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void validate() throws ValidatorException {
        Member member = getTableMemberWithInvalidForeignKey(mData.getTables());
        if (member != null) {
            throw new ValidatorException(member.getElement(), "[@Member foreign_key value must be either `_id` or a member variable of the annotated property]");
        }
    }
}
