package com.memtrip.sqlking.preprocessor.processor.validation;

import com.memtrip.sqlking.preprocessor.processor.Context;
import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.model.Member;
import com.memtrip.sqlking.preprocessor.processor.model.Table;
import com.memtrip.sqlking.preprocessor.processor.utils.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Each @Member annotations must have an associated getter / setter
 */
public class MemberAnnotationsHaveGetterSetters implements Validator {
    private Data mData;

    public MemberAnnotationsHaveGetterSetters(Data data) {
        mData = data;
    }

    public String getMemberWithoutField(Element element, List<Member> members) {
        List<? extends Element> elements = Context.getInstance().getElementUtils().getAllMembers((TypeElement)element);

        // Check that getters exist
        for (Member member : members) {
            if (!memberExistsAsField(member, elements, "get")) {
                return member.getName();
            }
        }

        // Check that setters exist
        for (Member member : members) {
            if (!memberExistsAsField(member, elements, "set")) {
                return member.getName();
            }
        }

        return null;
    }

    private boolean memberExistsAsField(Member member, List<? extends Element> elements, String fieldPrefix) {
        for (Element element : elements) {
            if (element.getSimpleName() != null && element.getSimpleName().toString().startsWith(fieldPrefix)) {
                String name = stripGetterFormatting(element.getSimpleName(), fieldPrefix);
                if (member.getName().equals(name)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String stripGetterFormatting(Name name, String prefix) {
        String value = name.toString();
        value = value.replace(prefix,"");
        value = StringUtils.firstToLowerCase(value);
        return value;
    }

    @Override
    public void validate() throws ValidatorException {
        for (Table table : mData.getTables()) {
            String memberNameWithoutField = getMemberWithoutField(table.getElement(), table.getMembers());
            if (memberNameWithoutField != null) {
                throw new ValidatorException(
                        table.getElement(),
                        "[@Member: `" + memberNameWithoutField + "` in @Table: `" + table.getName() + "` does not have an associated getter / setter]"
                );
            }
        }
    }
}
