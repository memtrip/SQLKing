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

import org.junit.Before;

import static com.memtrip.sqlking.operation.clause.Where.where;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class CountTest extends IntegrationTest {

    @Before
    public void setUp() {
        super.setUp();
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
        getSetupUser().setupFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
    public void testAllUsersAreCounted() {
        long count = Count.getBuilder().execute(User.class, getSQLProvider());
        assertEquals(4, count);
    }

    @org.junit.Test
    public void testEqualToCount() {
        long count = Count.getBuilder()
                .where(where(Q.User.TIMESTAMP, Where.Exp.EQUAL_TO, SetupUser.CLYDE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be counted
        assertEquals(1, count);
    }

    @org.junit.Test
    public void testMoreThanCount() {
        long count = Count.getBuilder()
                .where(where(Q.User.TIMESTAMP, Where.Exp.MORE_THAN, SetupUser.CLYDE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be counted
        assertEquals(3, count);
    }
}