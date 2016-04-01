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
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Select;

import org.junit.Before;

import static com.memtrip.sqlking.operation.clause.In.in;
import static com.memtrip.sqlking.operation.clause.Where.where;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class DeleteTest extends IntegrationTest {

    @Before
    public void setUp() {
        super.setUp();
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
        getSetupUser().setupFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
    public void testAllUsersAreDeleted() {
        int deletedRows = Delete.getBuilder().execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder().execute(User.class, getSQLProvider());

        // All of the 4 users created by #setupFourTestUsers will be deleted by the
        // exercise clause, therefore, we assert that 0 rows will be selected
        assertEquals(0, users.length);
        assertEquals(4, deletedRows);
    }

    @org.junit.Test
    public void testSingleUserIsDeleted() {
        int deletedRows = Delete.getBuilder()
            .where(where(Q.User.USERNAME, Where.Exp.EQUAL_TO, SetupUser.ANGIE_USER_NAME))
            .execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder().execute(User.class, getSQLProvider());

        // 1 of the 4 users created by #setupFourTestUsers will be deleted by the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
        assertEquals(1, deletedRows);
    }

    @org.junit.Test
    public void testUsersAreDeleted() {
        int deletedRows = Delete.getBuilder()
            .where(in(Q.User.USERNAME, SetupUser.ANGIE_USER_NAME, SetupUser.CLYDE_USER_NAME, SetupUser.GILL_USER_NAME))
            .execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder().execute(User.class, getSQLProvider());

        // 3 of the 4 users created by #setupFourTestUsers will be deleted by the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
        assertEquals(3, deletedRows);
    }
}