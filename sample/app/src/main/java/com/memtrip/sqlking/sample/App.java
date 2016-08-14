package com.memtrip.sqlking.sample;

import android.app.Application;

import com.memtrip.sqlking.database.SQLInit;
import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.gen.Q;
import com.memtrip.sqlking.sample.model.Comment;
import com.memtrip.sqlking.sample.model.User;

public class App extends Application {
    private static App sApp;

    private SQLProvider sqlProvider;

    private static final String DATABASE_NAME = "SQLKing";
    private static final int VERSION = 2;

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
                DATABASE_NAME,
                VERSION,
                new Q.DefaultResolver(),
                this,
                Comment.class,
                User.class
        );
    }
}