package com.beyondconstraint.sqlking.integration;

import android.content.ContentValues;

import com.beyondconstraint.sqlking.integration.models.User;
import com.beyondconstraint.sqlking.integration.utils.SetupUser;
import com.beyondconstraint.sqlking.operation.clause.Where;
import com.beyondconstraint.sqlking.operation.function.Select;
import com.beyondconstraint.sqlking.operation.function.Update;

import org.junit.After;
import org.junit.Before;

public class UpdateTest extends IntegrationTest {

    @Before
    public void setUp() {
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
        getSetupUser().setupFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
    public void testSingleUpdate() {
        // setup
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put("isRegistered", true);
        contentValues.put("timestamp", timestamp);

        // exercise
        Update.getBuilder()
                .values(contentValues)
                .where(new Where("username", Where.Exp.EQUAL_TO, SetupUser.CLYDE_USER_NAME))
                .execute(User.class, getSQLProvider());

        // verify
        User user = Select.getBuilder()
                .where(new Where("username", Where.Exp.EQUAL_TO, SetupUser.CLYDE_USER_NAME))
                .executeSingle(User.class, getSQLProvider());

        assertEquals(true, user.getIsRegistered());
        assertEquals(timestamp, user.getTimestamp());
    }

    @org.junit.Test
    public void testBulkUpdate() {
        // setup
        long timestamp = System.currentTimeMillis();
        String newUsername = "CHANGED";

        ContentValues contentValues = new ContentValues();
        contentValues.put("isRegistered", true);
        contentValues.put("timestamp", timestamp);
        contentValues.put("username", newUsername);

        // exercise
        Update.getBuilder()
                .values(contentValues)
                .execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder()
                .execute(User.class, getSQLProvider());

        for (User user : users) {
            assertEquals(timestamp, user.getTimestamp());
            assertEquals(true, user.getIsRegistered() );
            assertEquals(newUsername, user.getUsername());
        }
    }

    @org.junit.Test
    public void testMoreThanUpdate() {
        // setup
        long newTimestamp = 0;
        String newUsername = "CHANGED";

        ContentValues contentValues = new ContentValues();
        contentValues.put("isRegistered", true);
        contentValues.put("timestamp", newTimestamp);
        contentValues.put("username", newUsername);

        // exercise
        Update.getBuilder()
                .values(contentValues)
                .where(new Where("timestamp", Where.Exp.MORE_THAN, SetupUser.CLYDE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // verify
        User[] users = Select.getBuilder()
                .where(new Where("timestamp", Where.Exp.EQUAL_TO, newTimestamp))
                .execute(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        // with a timestamp of "0"
        assertEquals(3, users.length);
    }
}