package com.beyondconstraint.sqlking.unit;

import com.beyondconstraint.sqlking.database.DatabaseController;
import com.beyondconstraint.sqlking.integration.models.Post;
import com.beyondconstraint.sqlking.integration.models.User;
import com.beyondconstraint.sqlking.integration.utils.SetupUser;
import com.beyondconstraint.sqlking.operation.clause.And;
import com.beyondconstraint.sqlking.operation.clause.Clause;
import com.beyondconstraint.sqlking.operation.clause.In;
import com.beyondconstraint.sqlking.operation.clause.Or;
import com.beyondconstraint.sqlking.operation.clause.Where;
import com.beyondconstraint.sqlking.operation.keyword.Limit;
import com.beyondconstraint.sqlking.operation.keyword.OrderBy;
import com.beyondconstraint.sqlking.unit.mock.DatabaseControllerMock;

import static org.junit.Assert.assertEquals;

public class DatabaseControllerTest {

    @org.junit.Test
    public void testTableNameIsCreatedFromClassDefinition() {
        DatabaseController databaseController = new DatabaseControllerMock();

        String userTable = databaseController.getTableName(User.class);
        String postTable = databaseController.getTableName(Post.class);

        assertEquals("User", userTable);
        assertEquals("Post", postTable);
    }

    @org.junit.Test
    public void testTableNameIsCreatedFromObjectInstance() {
        DatabaseController databaseController = new DatabaseControllerMock();

        String userTable = databaseController.getTableName(new User());
        String postTable = databaseController.getTableName(new Post());

        assertEquals("User", userTable);
        assertEquals("Post", postTable);
    }

    @org.junit.Test
    public void testWhereQueryIsBuiltFromClauseCollection() {
        DatabaseController databaseController = new DatabaseControllerMock();

        Where where = new Where("username", Where.Exp.EQUAL_TO, "sam");

        String clause = databaseController.getClause(new Clause[]{where});
        String[] args = databaseController.getClauseArgs(new Clause[]{where});

        assertEquals("username = ?", clause);
        assertEquals(1, args.length);
        assertEquals("sam", args[0]);
    }

    @org.junit.Test
    public void testInQueryIsBuiltFromClauseCollection() {
        DatabaseController databaseController = new DatabaseControllerMock();

        In in = new In("username", "sam", "josh");

        String clause = databaseController.getClause(new Clause[]{in});
        String[] args = databaseController.getClauseArgs(new Clause[]{in});

        assertEquals("username IN (?,?)", clause);
        assertEquals(2, args.length);
        assertEquals("sam", args[0]);
        assertEquals("josh", args[1]);
    }

    @org.junit.Test
    public void testAndWhereQueryIsBuiltFromClauseCollection() {
        DatabaseController databaseController = new DatabaseControllerMock();

        And and = new And(
                new Where("timestamp", Where.Exp.MORE_THAN, 10),
                new Where("timestamp", Where.Exp.EQUAL_TO, 20)
        );

        String clause = databaseController.getClause(new Clause[]{and});
        String[] args = databaseController.getClauseArgs(new Clause[]{and});

        assertEquals("(timestamp > ? AND timestamp = ?)", clause);
        assertEquals(2, args.length);
        assertEquals("10", args[0]);
        assertEquals("20", args[1]);
    }

    @org.junit.Test
    public void tesOrAndWhereQueryIsBuiltFromClauseCollection() {
        DatabaseController databaseController = new DatabaseControllerMock();

        And and = new And(
                new Or(
                        new Where("username", Where.Exp.EQUAL_TO, "sam"),
                        new Where("username", Where.Exp.EQUAL_TO, "angie")
                ),
                new And(
                        new Where("timestamp", Where.Exp.MORE_THAN_OR_EQUAL_TO, 1234567890)
                )
        );

        String clause = databaseController.getClause(new Clause[]{and});
        String[] args = databaseController.getClauseArgs(new Clause[]{and});

        assertEquals("((username = ? OR username = ?) AND (timestamp >= ?))", clause);
        assertEquals(3, args.length);
        assertEquals("sam", args[0]);
        assertEquals("angie", args[1]);
        assertEquals("1234567890", args[2]);
    }

    @org.junit.Test
    public void testOrWhereInQueryIsBuiltFromClause() {
        DatabaseController databaseController = new DatabaseControllerMock();

        Or or = new Or(
            new Where("username", Where.Exp.EQUAL_TO, "sam"),
            new In("timestamp", 10, 20)
        );

        String clause = databaseController.getClause(new Clause[]{or});
        String[] args = databaseController.getClauseArgs(new Clause[]{or});

        assertEquals("(username = ? OR timestamp IN (?,?))", clause);
        assertEquals(3, args.length);
        assertEquals("sam", args[0]);
        assertEquals("10", args[1]);
        assertEquals("20", args[2]);
    }

    @org.junit.Test
    public void testOrderByAscBuiltFromClause() {
        DatabaseController databaseController = new DatabaseControllerMock();

        String orderBy = databaseController.getOrderBy(new OrderBy("username", OrderBy.Order.ASC));

        assertEquals("username ASC", orderBy);
    }

    @org.junit.Test
    public void testOrderByDescBuiltFromClause() {
        DatabaseController databaseController = new DatabaseControllerMock();

        String orderBy = databaseController.getOrderBy(new OrderBy("username", OrderBy.Order.DESC));

        assertEquals("username DESC", orderBy);
    }

    @org.junit.Test
    public void testLimitBuiltFromClause() {
        DatabaseController databaseController = new DatabaseControllerMock();

        String limit = databaseController.getLimit(new Limit(0, 1));

        assertEquals("0,1", limit);
    }

    @org.junit.Test
    public void testAllColumnsAreGeneratedFromTheModelClassDefinitions() {
        DatabaseController databaseController = new DatabaseControllerMock();

        String[] userColumnNames = databaseController.getSQLColumnNamesFromModel(User.class);
        String[] postColumnNames = databaseController.getSQLColumnNamesFromModel(Post.class);

        assertEquals(4, userColumnNames.length);
        assertEquals(4, postColumnNames.length);
    }
}