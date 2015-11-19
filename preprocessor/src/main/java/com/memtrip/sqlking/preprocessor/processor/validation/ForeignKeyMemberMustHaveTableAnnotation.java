package com.memtrip.sqlking.preprocessor.processor.validation;

import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.model.ForeignKey;
import com.memtrip.sqlking.preprocessor.processor.model.Member;
import com.memtrip.sqlking.preprocessor.processor.model.Table;

import java.util.List;

public class ForeignKeyMemberMustHaveTableAnnotation implements Validator {
    private Data mData;

    public ForeignKeyMemberMustHaveTableAnnotation(Data data) {
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
                ForeignKey foreignKey = member.getForeignKey();
                if (!foreignKeyIsAnnotatingTable(foreignKey, mData.getTables())) {
                    return member;
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
        Member member = getTableMemberWithInvalidForeignKey(mData.getTables());
        if (member != null) {
            throw new ValidatorException(member.getElement(), "[A @Member with a foreign_key can only annotate a variable whose data type is annotated with @Table]");
        }
    }
}
