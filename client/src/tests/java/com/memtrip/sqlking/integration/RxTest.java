package com.memtrip.sqlking.integration;

import android.content.ContentValues;

import com.memtrip.sqlking.gen.Q;
import com.memtrip.sqlking.integration.models.User;
import com.memtrip.sqlking.integration.utils.SetupUser;
import com.memtrip.sqlking.operation.clause.Where;
import com.memtrip.sqlking.operation.function.Count;
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;
import com.memtrip.sqlking.operation.function.Update;

import org.junit.Before;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.memtrip.sqlking.operation.clause.Where.where;

public class RxTest extends IntegrationTest {

    @Before
    public void setUp() {
        super.setUp();
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
        getSetupUser().setupFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
    public void testRxCreate() {
        // setup
        String USER_ID = "rx123456789";
        long USER_TIMESTAMP = System.currentTimeMillis();
        boolean USER_IS_REGISTERED = true;

        User user = new User();
        user.setUsername(USER_ID);
        user.setIsRegistered(USER_IS_REGISTERED);
        user.setTimestamp(USER_TIMESTAMP);

        // exercise
        Observable<Void> create = Insert.getBuilder().values(user).rx(getSQLProvider());
        TestSubscriber<Void> sub = new TestSubscriber<>();
        create.subscribe(sub);

        // verify
        sub.assertNoErrors();

        Observable<User> verify = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Exp.EQUAL_TO, USER_ID))
                .rxOne(User.class, getSQLProvider());

        TestSubscriber<User> subVerify = new TestSubscriber<>();
        verify.subscribe(subVerify);
        List<User> verifiedUsers = subVerify.getOnNextEvents();

        assertTrue(user.getUsername().equals(verifiedUsers.get(0).getUsername()));
        assertTrue(user.getTimestamp() == verifiedUsers.get(0).getTimestamp());
        assertTrue(user.getIsRegistered() == verifiedUsers.get(0).getIsRegistered());
    }

    @org.junit.Test
    public void testRxRead() {
        // exercise
        Observable<User[]> read = Select.getBuilder()
                .rx(User.class, getSQLProvider());

        TestSubscriber<User[]> sub = new TestSubscriber<>();
        read.subscribe(sub);
        List<User[]> users = sub.getOnNextEvents();

        // verify
        assertEquals(4, users.get(0).length);
    }

    @org.junit.Test
    public void testRxReadOne() {
        // exercise
        Observable<User> read = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Exp.EQUAL_TO, SetupUser.CLYDE_USER_NAME))
                .rxOne(User.class, getSQLProvider());

        TestSubscriber<User> sub = new TestSubscriber<>();
        read.subscribe(sub);
        List<User> user = sub.getOnNextEvents();

        // verify
        assertEquals(SetupUser.CLYDE_USER_NAME, user.get(0).getUsername());
    }

    @org.junit.Test
    public void testRxUpdate() {
        // setup
        long timestamp = System.currentTimeMillis();
        String newUsername = "CHANGED";

        ContentValues contentValues = new ContentValues();
        contentValues.put(Q.User.IS_REGISTERED, true);
        contentValues.put(Q.User.TIMESTAMP, timestamp);
        contentValues.put(Q.User.USERNAME, newUsername);

        // exercise
        Observable<Integer> update = Update.getBuilder()
                .values(contentValues)
                .rx(User.class, getSQLProvider());

        TestSubscriber<Integer> sub = new TestSubscriber<>();
        update.subscribe(sub);
        List<Integer> updateCount = sub.getOnNextEvents();

        // verify
        Observable<User[]> verify = Select.getBuilder().rx(User.class, getSQLProvider());
        TestSubscriber<User[]> subVerify = new TestSubscriber<>();
        verify.subscribe(subVerify);
        List<User[]> verifiedUsers = subVerify.getOnNextEvents();

        for (User user : verifiedUsers.get(0)) {
            assertEquals(timestamp, user.getTimestamp());
            assertEquals(true, user.getIsRegistered() );
            assertEquals(newUsername, user.getUsername());
        }

        assertEquals(updateCount.get(0).intValue(), verifiedUsers.get(0).length);
    }

    @org.junit.Test
    public void testRxDelete() {
        // exercise
        Observable<Integer> delete = Delete.getBuilder().rx(User.class, getSQLProvider());
        TestSubscriber<Integer> sub = new TestSubscriber<>();
        delete.subscribe(sub);
        List<Integer> deletedRows = sub.getOnNextEvents();

        // verify
        Observable<User[]> verify = Select.getBuilder().rx(User.class, getSQLProvider());
        TestSubscriber<User[]> subVerify = new TestSubscriber<>();
        verify.subscribe(subVerify);
        List<User[]> verifiedUsers = subVerify.getOnNextEvents();

        assertEquals(0, verifiedUsers.get(0).length);
        assertEquals(4, deletedRows.get(0).intValue());
    }

    @org.junit.Test
    public void testRxCount() {
        // exercise
        Observable<Long> count = Count.getBuilder().rx(User.class, getSQLProvider());
        TestSubscriber<Long> sub = new TestSubscriber<>();
        count.subscribe(sub);
        List<Long> countedRows = sub.getOnNextEvents();

        // verify
        assertEquals(4, countedRows.get(0).intValue());
    }
}
