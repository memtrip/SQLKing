/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memtrip.sqlking.integration.utils;

import android.content.Context;

import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.database.SQLInit;
import com.memtrip.sqlking.gen.Q;
import com.memtrip.sqlking.integration.models.Post;
import com.memtrip.sqlking.integration.models.User;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class Setup {
    private Context mContext;
    private SQLProvider mSQLProvider;

    public static final String DATABASE_NAME = "SQLKingTest";
    private static final int DATABASE_VERSION = 1;

    public SQLProvider getSQLProvider() {
        return mSQLProvider;
    }

    public Setup(Context context) {
        mContext = context;
    }

    public void setUp() {
        mSQLProvider = SQLInit.createDatabase(
                DATABASE_NAME,
                DATABASE_VERSION,
                new Q.DefaultResolver(),
                mContext,
                User.class,
                Post.class
        );
    }
}