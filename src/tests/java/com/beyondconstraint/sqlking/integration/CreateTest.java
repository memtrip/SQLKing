package com.beyondconstraint.sqlking.integration;

import com.beyondconstraint.sqlking.integration.models.User;
import com.beyondconstraint.sqlking.integration.utils.SetupUser;
import com.beyondconstraint.sqlking.operation.function.Insert;
import com.beyondconstraint.sqlking.operation.function.Select;
import com.beyondconstraint.sqlking.operation.clause.Where;

import org.junit.After;
import org.junit.Before;

public class CreateTest  extends IntegrationTest {

    @Before
    public void setUp() {
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
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
        long[] value = Insert.getBuilder().values(user).execute(getSQLProvider());

        // verify
        assertTrue(value.length > 0 && value[0] != -1);

        User responseUser = Select.getBuilder().execute(User.class, getSQLProvider())[0];

        assertTrue(user.getUsername().equals(responseUser.getUsername()));
        assertTrue(user.getTimestamp() == responseUser.getTimestamp());
        assertTrue(user.getIsRegistered() == responseUser.getIsRegistered());
    }

    @org.junit.Test
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
        long[] values = Insert.getBuilder().values(users).execute(getSQLProvider());

        // verify
        assertEquals(values.length, users.length);

        User angieUser = Select.getBuilder()
                .where(new Where("username", Where.Exp.EQUAL_TO, ANGIE_USERNAME))
                .executeSingle(User.class, getSQLProvider());

        User samUser = Select.getBuilder()
                .where(new Where("username", Where.Exp.EQUAL_TO, SAM_USERNAME))
                .executeSingle(User.class, getSQLProvider());

        assertEquals(ANGIE_USERNAME, angieUser.getUsername());
        assertEquals(ANGIE_TIMESTAMP, angieUser.getTimestamp());
        assertEquals(ANGIE_IS_REGISTERED, angieUser.getIsRegistered());

        assertEquals(SAM_USERNAME, samUser.getUsername());
        assertEquals(SAM_TIMESTAMP, samUser.getTimestamp());
        assertEquals(SAM_IS_REGISTERED, samUser.getIsRegistered());
    }
}