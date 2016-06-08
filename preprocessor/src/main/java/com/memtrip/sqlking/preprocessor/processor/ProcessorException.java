package com.memtrip.sqlking.preprocessor.processor;

import javax.lang.model.element.Element;

public class ProcessorException extends Exception {
    private Element mElement;

    public Element getElement() {
        return mElement;
    }

    public ProcessorException(String message, Element element) {
        super(message);
        mElement = element;
    }
}