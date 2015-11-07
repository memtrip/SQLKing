package com.beyondconstraint.sqlking.unit;

import android.content.ContentValues;
import android.database.Cursor;

import com.beyondconstraint.sqlking.Model;
import com.beyondconstraint.sqlking.database.Database;
import com.beyondconstraint.sqlking.database.DatabaseController;
import com.beyondconstraint.sqlking.database.DatabaseEngine;
import com.beyondconstraint.sqlking.database.DefaultDatabaseEngine;
import com.beyondconstraint.sqlking.database.InsertQuery;
import com.beyondconstraint.sqlking.integration.models.User;
import com.beyondconstraint.sqlking.operation.clause.Clause;
import com.beyondconstraint.sqlking.operation.keyword.Limit;
import com.beyondconstraint.sqlking.operation.keyword.OrderBy;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DatabaseEngineTest {

    @org.junit.Test
    public void testSingleRowIsInserted() {
        // setup
        final User user = new User();

        final String TABLE_NAME = "User";
        final ContentValues CONTENT_VALUES = new ContentValues();

        Database database = mock(Database.class);
        DatabaseEngine databaseEngine = new DefaultDatabaseEngine(database);

        DatabaseController databaseController = mock(DatabaseController.class);
        when(databaseController.getTableName(user)).thenReturn(TABLE_NAME);
        when(databaseController.getContentValuesFromModel(user)).thenReturn(CONTENT_VALUES);

        // exercise
        databaseEngine.insert(user, databaseController);

        // verify
        verify(databaseController, times(1)).getTableName(user);
        verify(databaseController, times(1)).getContentValuesFromModel(user);
        verify(database, times(1)).insert(TABLE_NAME, CONTENT_VALUES);
    }

    @org.junit.Test
    public void testMultipleRowsAreInserted() {
        // setup
        final User user = new User();
        final User[] users = new User[] {
            user, user, user, user
        };

        final String TABLE_NAME = "User";
        final ContentValues CONTENT_VALUES = new ContentValues();
        final InsertQuery INSERT_QUERY = new InsertQuery(TABLE_NAME, CONTENT_VALUES);
        final InsertQuery[] INSERT_QUERY_ARRAY = new InsertQuery[] {
            INSERT_QUERY,INSERT_QUERY,INSERT_QUERY,INSERT_QUERY
        };

        Database database = mock(Database.class);
        DatabaseEngine databaseEngine = new DefaultDatabaseEngine(database);

        DatabaseController databaseController = mock(DatabaseController.class);
        when(databaseController.getTableName(any(User.class))).thenReturn(TABLE_NAME);
        when(databaseController.createInsertQuery(user)).thenReturn(INSERT_QUERY);

        // exercise
        long[] ids = databaseEngine.insert(users, databaseController);

        // verify
        verify(databaseController, times(users.length)).createInsertQuery(any(User.class));
        verify(database, times(1)).insertMultiple(INSERT_QUERY_ARRAY);
    }

    @org.junit.Test
    public void testRowsAreUpdated() {
        // setup
        final Class<? extends Model> classDef = User.class;
        final ContentValues CONTENT_VALUES = new ContentValues();
        final Clause[] clause = new Clause[] { };

        final String TABLE_NAME = "User";
        final String QUERY_STRING = "<UPDATE>";
        final String[] QUERY_ARGS = new String[] { "ARG1", "ARG2" };

        Database database = mock(Database.class);
        DatabaseEngine databaseEngine = new DefaultDatabaseEngine(database);

        DatabaseController databaseController = mock(DatabaseController.class);
        when(databaseController.getTableName(classDef)).thenReturn(TABLE_NAME);
        when(databaseController.getClause(clause)).thenReturn(QUERY_STRING);
        when(databaseController.getClauseArgs(clause)).thenReturn(QUERY_ARGS);

        // exercise
        databaseEngine.update(classDef, CONTENT_VALUES, clause, databaseController);

        // verify
        verify(databaseController, times(1)).getTableName(classDef);
        verify(databaseController, times(1)).getClause(clause);
        verify(databaseController, times(1)).getClauseArgs(clause);
        verify(database, times(1)).update(TABLE_NAME, CONTENT_VALUES, QUERY_STRING, QUERY_ARGS);
    }

    @org.junit.Test
    public void testRowsAreSelected() {
        // setup
        final Class<? extends Model> classDef = User.class;
        final Clause[] clause = new Clause[] { };
        final OrderBy orderBy = new OrderBy("username", OrderBy.Order.ASC);
        final Limit limit = new Limit(0,10);

        final String TABLE_NAME = "User";
        final String[] SQL_COLUMNS = new String[] { };
        final String QUERY_STRING = "<SELECT>";
        final String[] QUERY_ARGS = new String[] { "ARG1", "ARG2" };
        final String ORDER_BY = "username ASC";
        final String LIMIT = "0,10";
        final Cursor CURSOR = null;

        Database database = mock(Database.class);
        DatabaseEngine databaseEngine = new DefaultDatabaseEngine(database);

        DatabaseController databaseController = mock(DatabaseController.class);
        when(databaseController.getTableName(classDef)).thenReturn(TABLE_NAME);
        when(databaseController.getSQLColumnNamesFromModel(classDef)).thenReturn(SQL_COLUMNS);
        when(databaseController.getClause(clause)).thenReturn(QUERY_STRING);
        when(databaseController.getClauseArgs(clause)).thenReturn(QUERY_ARGS);
        when(databaseController.getOrderBy(orderBy)).thenReturn(ORDER_BY);
        when(databaseController.getLimit(limit)).thenReturn(LIMIT);
        when(database.query(TABLE_NAME, SQL_COLUMNS, QUERY_STRING, QUERY_ARGS, null, null, ORDER_BY, LIMIT)).thenReturn(CURSOR);

        // exercise
        databaseEngine.select(classDef, clause, orderBy, limit, databaseController);

        // verify
        verify(databaseController, times(1)).getTableName(classDef);
        verify(databaseController, times(1)).getSQLColumnNamesFromModel(classDef);
        verify(databaseController, times(1)).getClause(clause);
        verify(databaseController, times(1)).getClauseArgs(clause);
        verify(databaseController, times(1)).getOrderBy(orderBy);
        verify(databaseController, times(1)).getLimit(limit);
        verify(databaseController, times(1)).retrieveSQLSelectResults(classDef,CURSOR);
    }

    @org.junit.Test
    public void testRowsAreDeleted() {
        // setup
        Class<? extends Model> classDef = User.class;
        final Clause[] clause = new Clause[] { };

        final String TABLE_NAME = "User";
        final String QUERY_STRING = "<DELETE>";
        final String[] QUERY_ARGS = new String[] { "ARG1", "ARG2" };

        Database database = mock(Database.class);
        DatabaseEngine databaseEngine = new DefaultDatabaseEngine(database);

        DatabaseController databaseController = mock(DatabaseController.class);
        when(databaseController.getTableName(classDef)).thenReturn(TABLE_NAME);
        when(databaseController.getClause(clause)).thenReturn(QUERY_STRING);
        when(databaseController.getClauseArgs(clause)).thenReturn(QUERY_ARGS);

        // exercise
        databaseEngine.delete(classDef, clause, databaseController);

        // verify
        verify(databaseController, times(1)).getTableName(classDef);
        verify(databaseController, times(1)).getClause(clause);
        verify(databaseController, times(1)).getClauseArgs(clause);
        verify(database, times(1)).delete(TABLE_NAME, QUERY_STRING, QUERY_ARGS);
    }

    @org.junit.Test
    public void testRowsAreCounted() {
        // setup
        final Class<? extends Model> classDef = User.class;
        final Clause[] clause = new Clause[] { };

        final String TABLE_NAME = "User";
        final String QUERY_STRING = "<COUNT>";
        final String[] QUERY_ARGS = new String[] { };

        Database database = mock(Database.class);
        DatabaseEngine databaseEngine = new DefaultDatabaseEngine(database);

        DatabaseController databaseController = mock(DatabaseController.class);
        when(databaseController.getTableName(classDef)).thenReturn(TABLE_NAME);
        when(databaseController.getClause(clause)).thenReturn(QUERY_STRING);
        when(databaseController.getClauseArgs(clause)).thenReturn(QUERY_ARGS);

        // exercise
        databaseEngine.count(classDef, clause, databaseController);

        // verify
        verify(databaseController, times(1)).getTableName(classDef);
        verify(databaseController, times(1)).getClauseArgs(clause);
        verify(databaseController, times(1)).getClause(clause);
        verify(database, times(1)).count(TABLE_NAME, QUERY_STRING, QUERY_ARGS);
    }
}