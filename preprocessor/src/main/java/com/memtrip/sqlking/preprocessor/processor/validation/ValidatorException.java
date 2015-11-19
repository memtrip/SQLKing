package com.memtrip.sqlking.preprocessor.processor.validation;

import javax.lang.model.element.Element;

public class ValidatorException extends Exception {
    private Element mElement;

    public Element getElement() {
        return mElement;
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(Element element, String message) {
        super(message);
        mElement = element;
    }
}