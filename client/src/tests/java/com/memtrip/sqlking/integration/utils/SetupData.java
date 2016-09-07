package com.memtrip.sqlking.integration.utils;

import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.integration.models.Data;
import com.memtrip.sqlking.integration.models.Log;
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Insert;

public class SetupData {

    public void tearDownTestData(SQLProvider sqlProvider) {
        Delete.getBuilder().execute(Log.class, sqlProvider);
    }

    public void setupTestData(SQLProvider sqlProvider) {
        Data[] data = {
                createData(
                    "data1"
                ),
                createData(
                    "data2"
                ),
                createData(
                    "data3"
                ),
        };

        Insert.getBuilder()
                .values(data)
                .execute(sqlProvider);
    }

    public static Data createData(String name) {
        Data data = new Data();
        data.setName(name);
        return data;
    }
}
