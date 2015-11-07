package com.beyondconstraint.sqlking.integration.utils;

import android.content.Context;

import com.beyondconstraint.sqlking.Model;
import com.beyondconstraint.sqlking.database.DefaultDatabaseEngine;
import com.beyondconstraint.sqlking.database.SQLInit;
import com.beyondconstraint.sqlking.database.SQLProvider;
import com.beyondconstraint.sqlking.integration.models.Post;
import com.beyondconstraint.sqlking.integration.models.User;

public class Setup {
    private Context mContext;
    private SQLProvider mSQLProvider;

    private static final String DATABASE_NAME = "SQLKing";
    private static final int DATABASE_VERSION = 1;

    public SQLProvider getSQLProvider() {
        return mSQLProvider;
    }

    public Setup(Context context) {
        mContext = context;
    }

    public void setUp() {

        Model[] model = new Model[] {
                new Post(),
                new User()
        };

        SQLInit SQLInit = new SQLInit(
                DATABASE_NAME,
                DATABASE_VERSION,
                model,
                mContext
        );

        mSQLProvider = new SQLProvider(new DefaultDatabaseEngine(SQLInit.getDatabase()));
    }
}