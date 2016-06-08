package com.memtrip.sqlking.integration.utils;

import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.integration.models.Log;
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Insert;

public class SetupLog {
    public static final int LOG_1_ID = 1;
    public static final long LOG_1_TIMESTAMP = 123456789;

    public void tearDownTestLogs(SQLProvider sqlProvider) {
        Delete.getBuilder().execute(Log.class, sqlProvider);
    }

    public void setupTestLogs(SQLProvider sqlProvider) {
        Log[] logs = {
                createLog(
                        LOG_1_ID,
                        LOG_1_TIMESTAMP
                )
        };

        Insert.getBuilder()
                .values(logs)
                .execute(sqlProvider);
    }

    public static Log createLog(int id, long timestamp) {
        Log log = new Log();
        log.setId(id);
        log.setTimestamp(timestamp);
        return log;
    }
}
