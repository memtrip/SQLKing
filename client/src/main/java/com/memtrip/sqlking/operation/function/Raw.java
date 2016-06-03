package com.memtrip.sqlking.operation.function;

import android.database.Cursor;

import com.memtrip.sqlking.database.Query;
import com.memtrip.sqlking.database.SQLProvider;

public class Raw extends Query {

    private Raw() {

    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String mQuery;

        public Builder query(String query) {
            mQuery = query;
            return this;
        }

        public Cursor execute(SQLProvider sqlProvider) {
            return rawQuery(mQuery, sqlProvider);
        }
    }
}
