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
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;

import org.junit.Before;
import org.junit.Test;

import static com.memtrip.sqlking.operation.clause.Where.where;

/**
 * @author Samuel Kirton [sam@memtrip.com]
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
        Insert.getBuilder().values(user).execute(getSQLProvider());

        // verify
        User responseUser = Select.getBuilder().executeOne(User.class, getSQLProvider());

        assertTrue(user.getUsername().equals(responseUser.getUsername()));
        assertTrue(user.getTimestamp() == responseUser.getTimestamp());
        assertTrue(user.getIsRegistered() == responseUser.getIsRegistered());
    }

    @Test
    public void testMultipleInsert() {
        // setup
        int ANGIE_ID = 1;
        String ANGIE_USERNAME = "angie";
        long ANGIE_TIMESTAMP = System.currentTimeMillis();
        boolean ANGIE_IS_REGISTERED = true;
        double ANGIE_RATING = 2.7;
        int ANGIE_COUNT = 1028;

        int SAM_ID = 2;
        String SAM_USERNAME = "sam";
        long SAM_TIMESTAMP = System.currentTimeMillis() + 1000;
        boolean SAM_IS_REGISTERED = false;
        double SAM_RATING = 2.7;
        int SAM_COUNT = 10024;

        User[] users = new User[] {
                SetupUser.createUser(
                        ANGIE_ID,
                        ANGIE_USERNAME,
                        ANGIE_TIMESTAMP,
                        ANGIE_IS_REGISTERED,
                        ANGIE_RATING,
                        ANGIE_COUNT,
                        0
                ),

                SetupUser.createUser(
                        SAM_ID,
                        SAM_USERNAME,
                        SAM_TIMESTAMP,
                        SAM_IS_REGISTERED,
                        SAM_RATING,
                        SAM_COUNT,
                        0
                ),
        };

        // exercise
        Insert.getBuilder().values(users).execute(getSQLProvider());

        // verify
        User angieUser = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Exp.EQUAL_TO, ANGIE_USERNAME))
                .executeOne(User.class, getSQLProvider());

        User samUser = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Exp.EQUAL_TO, SAM_USERNAME))
                .executeOne(User.class, getSQLProvider());

        assertEquals(ANGIE_USERNAME, angieUser.getUsername());
        assertEquals(ANGIE_TIMESTAMP, angieUser.getTimestamp());
        assertEquals(ANGIE_IS_REGISTERED, angieUser.getIsRegistered());
        assertEquals(ANGIE_RATING, angieUser.getRating());
        assertEquals(ANGIE_COUNT, angieUser.getCount());

        assertEquals(SAM_USERNAME, samUser.getUsername());
        assertEquals(SAM_TIMESTAMP, samUser.getTimestamp());
        assertEquals(SAM_IS_REGISTERED, samUser.getIsRegistered());
        assertEquals(SAM_RATING, samUser.getRating());
        assertEquals(SAM_COUNT, samUser.getCount());
    }

    @Test
    public void testMoreThan500RowInsert() {
        int COLUMN_COUNT = 1350;
        int ANGIE_ID = 1;
        String ANGIE_USERNAME = "angie";
        long ANGIE_TIMESTAMP = System.currentTimeMillis();
        boolean ANGIE_IS_REGISTERED = true;
        double ANGIE_RATING = 1.0;
        int ANGIE_COUNT = 300;

        User[] users = new User[COLUMN_COUNT];
        for (int i = 0; i < COLUMN_COUNT; i++) {
            users[i] = SetupUser.createUser(
                    ANGIE_ID,
                    ANGIE_USERNAME,
                    ANGIE_TIMESTAMP+i,
                    ANGIE_IS_REGISTERED,
                    ANGIE_RATING,
                    ANGIE_COUNT,
                    0
            );
        }

        Insert.getBuilder().values(users).execute(getSQLProvider());

        User[] usersInserted = Select.getBuilder().execute(User.class, getSQLProvider());

        for (int i = 0; i < usersInserted.length; i++) {
            assertEquals(ANGIE_TIMESTAMP+i,usersInserted[i].getTimestamp());
        }

        assertEquals(COLUMN_COUNT, usersInserted.length);
    }
}