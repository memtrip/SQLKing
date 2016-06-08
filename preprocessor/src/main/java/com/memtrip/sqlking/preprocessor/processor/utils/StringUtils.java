package com.memtrip.sqlking.preprocessor.processor.utils;

public class StringUtils {

    public static String firstToUpperCase(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    public static String firstToLowerCase(String value) {
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    public static String assembleTypeGetter(String type) {
        switch (type) {
            case "java.lang.String":
                return "cursor.getString(x)";
            case "long":
                return "cursor.getLong(x)";
            case "int":
                return "cursor.getInt(x)";
            case "boolean":
                return "cursor.getInt(x) == 1 ? true : false";
            case "double":
                return "cursor.getDouble(x)";
            case "byte[]":
                return "cursor.getBlob(x)";
            default:
                return ""; // TODO: foreign key object
        }
    }
}
