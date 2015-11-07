package com.beyondconstraint.sqlking.integration;

import com.beyondconstraint.sqlking.integration.models.User;
import com.beyondconstraint.sqlking.integration.utils.SetupUser;
import com.beyondconstraint.sqlking.operation.function.Delete;
import com.beyondconstraint.sqlking.operation.function.Select;
import com.beyondconstraint.sqlking.operation.clause.In;
import com.beyondconstraint.sqlking.operation.clause.Where;

import org.junit.After;
import org.junit.Before;

public class DeleteTest extends IntegrationTest {

    @Before
    public void setUp() {
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
        getSetupUser().setupFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
    public void testAllUsersAreDeleted() {
        Delete.getBuilder().execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder().execute(User.class, getSQLProvider());

        // All of the 4 users created by #setupFourTestUsers will be deleted by the
        // exercise clause, therefore, we assert that 0 rows will be selected
        assertEquals(0, users.length);
    }

    @org.junit.Test
    public void testSingleUserIsDeleted() {
        Delete.getBuilder()
            .where(new Where("username", Where.Exp.EQUAL_TO, SetupUser.ANGIE_USER_NAME))
            .execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder().execute(User.class, getSQLProvider());

        // 1 of the 4 users created by #setupFourTestUsers will be deleted by the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testUsersAreDeleted() {
        Delete.getBuilder()
            .where(new In("username", SetupUser.ANGIE_USER_NAME, SetupUser.CLYDE_USER_NAME, SetupUser.GILL_USER_NAME))
            .execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder().execute(User.class, getSQLProvider());

        // 3 of the 4 users created by #setupFourTestUsers will be deleted by the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }
}