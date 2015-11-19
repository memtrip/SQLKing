package com.memtrip.sqlking.preprocessor.processor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class Context {
    private Types mTypes;
    private Elements mElements;
    private Filer mFiler;
    private Messager mMessager;

    private static Context sInstance;

    public Types getTypeUtils() {
        return mTypes;
    }

    public Elements getElementUtils() {
        return mElements;
    }

    public Filer getFiler() {
        return mFiler;
    }

    public Messager getMessager() {
        return mMessager;
    }

    public static void createInstance(ProcessingEnvironment env) {
        sInstance = new Context(env);
    }

    public static Context getInstance() {
        if (sInstance == null)
            throw new IllegalStateException("createInstance must be called before getInstance()");

        return sInstance;
    }

    private Context(ProcessingEnvironment env) {
        mTypes = env.getTypeUtils();
        mElements = env.getElementUtils();
        mFiler = env.getFiler();
        mMessager = env.getMessager();
    }
}