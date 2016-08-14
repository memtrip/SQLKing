package com.memtrip.sqlking.preprocessor.processor;

import javax.lang.model.element.Element;

public class ValidatorException extends Exception {
    private Element mElement;

    Element getElement() {
        return mElement;
    }

    ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(Element element, String message) {
        super(message);
        mElement = element;
    }
}