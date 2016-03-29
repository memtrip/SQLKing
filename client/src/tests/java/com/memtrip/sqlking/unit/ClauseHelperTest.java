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
package com.memtrip.sqlking.unit;

import com.memtrip.sqlking.database.ClauseHelper;
import com.memtrip.sqlking.gen.Q;
import com.memtrip.sqlking.operation.clause.And;
import com.memtrip.sqlking.operation.clause.Clause;
import com.memtrip.sqlking.operation.clause.In;
import com.memtrip.sqlking.operation.clause.Or;
import com.memtrip.sqlking.operation.clause.Where;
import com.memtrip.sqlking.operation.keyword.Limit;
import com.memtrip.sqlking.operation.keyword.OrderBy;
import com.memtrip.sqlking.unit.mock.ClauseHelperStub;

import static com.memtrip.sqlking.operation.clause.And.and;
import static com.memtrip.sqlking.operation.clause.In.in;
import static com.memtrip.sqlking.operation.clause.Or.or;
import static com.memtrip.sqlking.operation.clause.Where.where;
import static org.junit.Assert.assertEquals;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class ClauseHelperTest {

    @org.junit.Test
    public void testWhereQueryIsBuiltFromClauseCollection() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        Where where = where(Q.User.USERNAME, Where.Exp.EQUAL_TO, "sam");

        String clause = clauseHelper.getClause(new Clause[]{where});
        String[] args = clauseHelper.getClauseArgs(new Clause[]{where});

        assertEquals("username = ?", clause);
        assertEquals(1, args.length);
        assertEquals("sam", args[0]);
    }

    @org.junit.Test
    public void testInQueryIsBuiltFromClauseCollection() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        In in = in(Q.User.USERNAME, "sam", "josh");

        String clause = clauseHelper.getClause(new Clause[]{in});
        String[] args = clauseHelper.getClauseArgs(new Clause[]{in});

        assertEquals("username IN (?,?)", clause);
        assertEquals(2, args.length);
        assertEquals("sam", args[0]);
        assertEquals("josh", args[1]);
    }

    @org.junit.Test
    public void testAndWhereQueryIsBuiltFromClauseCollection() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        And and = and(
                where(Q.User.TIMESTAMP, Where.Exp.MORE_THAN, 10),
                where(Q.User.TIMESTAMP, Where.Exp.EQUAL_TO, 20)
        );

        String clause = clauseHelper.getClause(new Clause[]{and});
        String[] args = clauseHelper.getClauseArgs(new Clause[]{and});

        assertEquals("(timestamp > ? AND timestamp = ?)", clause);
        assertEquals(2, args.length);
        assertEquals("10", args[0]);
        assertEquals("20", args[1]);
    }

    @org.junit.Test
    public void tesOrAndWhereQueryIsBuiltFromClauseCollection() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        And and = and(
                or(
                        where(Q.User.USERNAME, Where.Exp.EQUAL_TO, "sam"),
                        where(Q.User.USERNAME, Where.Exp.EQUAL_TO, "angie")
                ),
                and(
                        where(Q.User.TIMESTAMP, Where.Exp.MORE_THAN_OR_EQUAL_TO, 1234567890)
                )
        );

        String clause = clauseHelper.getClause(new Clause[]{and});
        String[] args = clauseHelper.getClauseArgs(new Clause[]{and});

        assertEquals("((username = ? OR username = ?) AND (timestamp >= ?))", clause);
        assertEquals(3, args.length);
        assertEquals("sam", args[0]);
        assertEquals("angie", args[1]);
        assertEquals("1234567890", args[2]);
    }

    @org.junit.Test
    public void testOrWhereInQueryIsBuiltFromClause() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        Or or = or(
            where(Q.User.USERNAME, Where.Exp.EQUAL_TO, "sam"),
            in(Q.User.TIMESTAMP, 10, 20)
        );

        String clause = clauseHelper.getClause(new Clause[]{or});
        String[] args = clauseHelper.getClauseArgs(new Clause[]{or});

        assertEquals("(username = ? OR timestamp IN (?,?))", clause);
        assertEquals(3, args.length);
        assertEquals("sam", args[0]);
        assertEquals("10", args[1]);
        assertEquals("20", args[2]);
    }

    @org.junit.Test
    public void testOrderByAscBuiltFromClause() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        String orderBy = clauseHelper.getOrderBy(new OrderBy(Q.User.USERNAME, OrderBy.Order.ASC));

        assertEquals("username ASC", orderBy);
    }

    @org.junit.Test
    public void testOrderByDescBuiltFromClause() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        String orderBy = clauseHelper.getOrderBy(new OrderBy(Q.User.USERNAME, OrderBy.Order.DESC));

        assertEquals("username DESC", orderBy);
    }

    @org.junit.Test
    public void testLimitBuiltFromClause() {
        ClauseHelper clauseHelper = new ClauseHelperStub();

        String limit = clauseHelper.getLimit(new Limit(0, 1));

        assertEquals("0,1", limit);
    }
}