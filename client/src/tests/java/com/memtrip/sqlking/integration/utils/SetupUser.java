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

import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.integration.models.User;
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Insert;

/**
 * The test data is a hard dependency of all the integration tests, changing
 * these constants will cause failures in the ReadTest suite. It is advised that
 * new constants are added if further test suites are required
 * @author Samuel Kirton <a href="mailto:sam@memtrip.com" />
 */
public class SetupUser {
    public static final String ANGIE_USER_NAME = "angie";
    public static final long ANGIE_TIMESTAMP = 123456789;
    public static final boolean ANGIE_IS_REGISTERED = true;

    public static final String JOSH_USER_NAME = "josh";
    public static final long JOSH_TIMESTAMP = 23456789;
    public static final boolean JOSH_IS_REGISTERED = true;

    public static final String GILL_USER_NAME = "gill";
    public static final long GILL_TIMESTAMP = 3456789;
    public static final boolean GILL_IS_REGISTERED = false;

    public static final String CLYDE_USER_NAME = "clyde";
    public static final long CLYDE_TIMESTAMP = 456789;
    public static final boolean CLYDE_IS_REGISTERED = false;

    public void setupFourTestUsers(SQLProvider sqlProvider) {
        User[] users = {
            createUser(CLYDE_USER_NAME,CLYDE_TIMESTAMP,CLYDE_IS_REGISTERED),
            createUser(ANGIE_USER_NAME,ANGIE_TIMESTAMP,ANGIE_IS_REGISTERED),
            createUser(GILL_USER_NAME,GILL_TIMESTAMP,GILL_IS_REGISTERED),
            createUser(JOSH_USER_NAME,JOSH_TIMESTAMP,JOSH_IS_REGISTERED),
        };

        Insert.getBuilder()
            .values(users)
            .execute(User.class, sqlProvider);
    }

    public void tearDownFourTestUsers(SQLProvider sqlProvider) {
        Delete.getBuilder().execute(User.class, sqlProvider);
    }

    public static User createUser(String userId, long timestamp, boolean isRegistered) {
        User user = new User();
        user.setUsername(userId);
        user.setTimestamp(timestamp);
        user.setIsRegistered(isRegistered);
        return user;
    }
}