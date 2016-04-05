package com.memtrip.sqlking.preprocessor.processor.validation;

import com.memtrip.sqlking.preprocessor.processor.Context;
import com.memtrip.sqlking.preprocessor.processor.model.Column;
import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.model.Table;
import com.memtrip.sqlking.preprocessor.processor.utils.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Each @Column annotations must have an associated getter / setter
 */
public class MemberAnnotationsHaveGetterSetters implements Validator {
    private Data mData;

    public MemberAnnotationsHaveGetterSetters(Data data) {
        mData = data;
    }

    public String getMemberWithoutField(Element element, List<Column> columns) {
        List<? extends Element> elements = Context.getInstance().getElementUtils().getAllMembers((TypeElement)element);

        // Check that getters exist
        for (Column column : columns) {
            if (!memberExistsAsField(column, elements, "get")) {
                return column.getName();
            }
        }

        // Check that setters exist
        for (Column column : columns) {
            if (!memberExistsAsField(column, elements, "set")) {
                return column.getName();
            }
        }

        return null;
    }

    private boolean memberExistsAsField(Column column, List<? extends Element> elements, String fieldPrefix) {
        for (Element element : elements) {
            if (element.getSimpleName() != null && element.getSimpleName().toString().startsWith(fieldPrefix)) {
                String name = stripGetterFormatting(element.getSimpleName(), fieldPrefix);
                if (column.getName().equals(name)) {
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
            String memberNameWithoutField = getMemberWithoutField(table.getElement(), table.getColumns());
            if (memberNameWithoutField != null) {
                throw new ValidatorException(
                        table.getElement(),
                        "[@Column: `" + memberNameWithoutField + "` in @Table: `" + table.getName() + "` does not have an associated getter / setter]"
                );
            }
        }
    }
}
