package com.memtrip.sqlking.preprocessor.processor.freemarker;

import com.memtrip.sqlking.preprocessor.processor.data.Data;
import com.memtrip.sqlking.preprocessor.processor.freemarker.method.*;

import java.util.HashMap;
import java.util.Map;

public class DataModel {

    private static final String TABLES = "tables";

    public static Map<String, Object> create(Data data) {
        Map<String, Object> map = new HashMap<>();

        map.put(TABLES, data.getTables());
        map.putAll(GetCursorGetterMethod.getMethodMap());
        map.putAll(GetInsertValueMethod.getMethodMap());
        map.putAll(AssembleCreateTableMethod.getMethodMap());
        map.putAll(FormatConstantMethod.getMethodMap());
        map.putAll(JoinSettersMethod.getMethodMap());
        map.putAll(JoinReferencesMethod.getMethodMap());

        return map;
    }
}
