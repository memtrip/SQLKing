package com.memtrip.sqlking.preprocessor.processor.utils;

public class StringUtils {

    public static String firstToUpperCase(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    public static String firstToLowerCase(String value) {
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }
}
