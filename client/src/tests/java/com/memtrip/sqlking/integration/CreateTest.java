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
package com.memtrip.sqlking.integration;

import com.memtrip.sqlking.gen.Q;
import com.memtrip.sqlking.integration.models.User;
import com.memtrip.sqlking.integration.utils.SetupUser;
import com.memtrip.sqlking.operation.clause.Where;
import com.memtrip.sqlking.operation.function.Count;
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Samuel Kirton <a href="mailto:sam@memtrip.com" />
 */
public class CreateTest  extends IntegrationTest {

    @Before
    public void setUp() {
        super.setUp();
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
    }

    @Test
    public void testSingleInsert() {
        // setup
        String USER_ID = "1234567890";
        long USER_TIMESTAMP = System.currentTimeMillis();
        boolean USER_IS_REGISTERED = true;

        User user = new User();
        user.setUsername(USER_ID);
        user.setIsRegistered(USER_IS_REGISTERED);
        user.setTimestamp(USER_TIMESTAMP);

        // exercise
        Insert.getBuilder().values(user).execute(User.class, getSQLProvider());

        // verify
        User responseUser = Select.getBuilder().execute(User.class, getSQLProvider())[0];

        assertTrue(user.getUsername().equals(responseUser.getUsername()));
        assertTrue(user.getTimestamp() == responseUser.getTimestamp());
        assertTrue(user.getIsRegistered() == responseUser.getIsRegistered());
    }

    @Test
    public void testMultipleInsert() {
        // setup
        String ANGIE_USERNAME = "angie";
        long ANGIE_TIMESTAMP = System.currentTimeMillis();
        boolean ANGIE_IS_REGISTERED = true;

        String SAM_USERNAME = "sam";
        long SAM_TIMESTAMP = System.currentTimeMillis() + 1000;
        boolean SAM_IS_REGISTERED = false;

        User[] users = new User[] {
                SetupUser.createUser(ANGIE_USERNAME, ANGIE_TIMESTAMP, ANGIE_IS_REGISTERED),
                SetupUser.createUser(SAM_USERNAME, SAM_TIMESTAMP, SAM_IS_REGISTERED),
        };

        // exercise
        Insert.getBuilder().values(users).execute(User.class, getSQLProvider());

        // verify
        User angieUser = Select.getBuilder()
                .where(new Where(Q.UserSQLQuery.USERNAME, Where.Exp.EQUAL_TO, ANGIE_USERNAME))
                .executeSingle(User.class, getSQLProvider());

        User samUser = Select.getBuilder()
                .where(new Where(Q.UserSQLQuery.USERNAME, Where.Exp.EQUAL_TO, SAM_USERNAME))
                .executeSingle(User.class, getSQLProvider());

        assertEquals(ANGIE_USERNAME, angieUser.getUsername());
        assertEquals(ANGIE_TIMESTAMP, angieUser.getTimestamp());
        assertEquals(ANGIE_IS_REGISTERED, angieUser.getIsRegistered());

        assertEquals(SAM_USERNAME, samUser.getUsername());
        assertEquals(SAM_TIMESTAMP, samUser.getTimestamp());
        assertEquals(SAM_IS_REGISTERED, samUser.getIsRegistered());
    }

    @Test
    public void testMoreThan500RowInsert() {
        int COLUMN_COUNT = 1350;
        String ANGIE_USERNAME = "angie";
        long ANGIE_TIMESTAMP = System.currentTimeMillis();
        boolean ANGIE_IS_REGISTERED = true;

        User[] users = new User[COLUMN_COUNT];
        for (int i = 0; i < COLUMN_COUNT; i++) {
            users[i] = SetupUser.createUser(
                    ANGIE_USERNAME,
                    ANGIE_TIMESTAMP+i,
                    ANGIE_IS_REGISTERED
            );
        }

        Insert.getBuilder().values(users).execute(User.class, getSQLProvider());

        User[] usersInserted = Select.getBuilder().execute(User.class, getSQLProvider());

        for (int i = 0; i < usersInserted.length; i++) {
            assertEquals(ANGIE_TIMESTAMP+i,usersInserted[i].getTimestamp());
        }

        assertEquals(COLUMN_COUNT, usersInserted.length);
    }
}