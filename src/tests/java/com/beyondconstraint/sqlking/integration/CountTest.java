package com.beyondconstraint.sqlking.integration;

import com.beyondconstraint.sqlking.integration.models.User;
import com.beyondconstraint.sqlking.integration.utils.SetupUser;
import com.beyondconstraint.sqlking.operation.clause.Where;
import com.beyondconstraint.sqlking.operation.function.Count;

import org.junit.After;
import org.junit.Before;

public class CountTest extends IntegrationTest {

    @Before
    public void setUp() {
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
                .where(new Where("timestamp", Where.Exp.EQUAL_TO, SetupUser.CLYDE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be counted
        assertEquals(1, count);
    }

    @org.junit.Test
    public void testMoreThanCount() {
        long count = Count.getBuilder()
                .where(new Where("timestamp", Where.Exp.MORE_THAN, SetupUser.CLYDE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be counted
        assertEquals(3, count);
    }
}