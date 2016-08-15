package com.memtrip.sqlking.database;

import android.util.Log;

class Logger {

    private static final String PREFIX = "SQLKing :: \n";

    static void logQuery(String query, boolean debugMode) {
        if (debugMode) {
            Log.d(PREFIX, query);
        }
    }

    static void logQuery(String table, String[] columns, String selection,
                    String[] selectionArgs, String groupBy,
                    String having, String orderBy, String limit, boolean debugMode) {

        if (debugMode) {
            // TODO: build a select statement log with the provided arguments
            StringBuilder sb = new StringBuilder();
            Log.d(PREFIX, sb.toString());
        }
    }
}
