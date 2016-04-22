package com.memtrip.sqlking.sample;

import android.app.Application;

import com.memtrip.sqlking.database.SQLInit;
import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.gen.Q;
import com.memtrip.sqlking.sample.model.Comment;

public class App extends Application {
    private static App sApp;

    private SQLProvider sqlProvider;

    public SQLProvider getSQLProvider() {
        return sqlProvider;
    }

    public static App getInstance() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sApp = this;

        sqlProvider = SQLInit.createDatabase(
                "SQLKing",
                1,
                new Q.DefaultResolver(),
                this,
                Comment.class
        );
    }
}