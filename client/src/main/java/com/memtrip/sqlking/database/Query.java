package com.memtrip.sqlking.database;

import android.database.Cursor;

import com.memtrip.sqlking.common.SQLQuery;
import com.memtrip.sqlking.operation.function.Count;
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;
import com.memtrip.sqlking.operation.function.Update;

public abstract class Query {

    protected static void insert(Insert insert, Class<?> classDef, SQLProvider database) {
        if (insert.getModels() != null && insert.getModels().length > 0) {
            String[] unionInsert = getSQLQuery(classDef,database).buildUnionInsertQuery(insert.getModels());
            database.insertMultiple(unionInsert);
        }
    }

    protected static <T> T[] select(Select select, Class<?> classDef, SQLProvider database) {
        SQLQuery sqlQuery = getSQLQuery(classDef, database);

        Cursor cursor = database.query(
                sqlQuery.getTableName(),
                sqlQuery.getColumnNames(),
                select.getClause(),
                null,
                null,
                select.getOrderBy(),
                select.getLimit()
        );

        return sqlQuery.retrieveSQLSelectResults(cursor);
    }

    protected static <T> T selectSingle(Select select, Class<?> classDef, SQLProvider database) {
        T[] results = select(select, classDef, database);

        if (results != null && results.length > 0) {
            return results[0];
        } else {
            return null;
        }
    }

    protected static boolean update(Update update, Class<?> classDef, SQLProvider database) {
        int result = database.update(
                getSQLQuery(classDef, database).getTableName(),
                update.getContentValues(),
                update.getConditions()
        );

        return result >= 1;
    }

    protected static long count(Count count, Class<?> classDef, SQLProvider database) {
        return database.count(
                getSQLQuery(classDef, database).getTableName(),
                count.getClause()
        );
    }

    protected static int delete(Delete delete, Class<?> classDef, SQLProvider database) {
        return database.delete(
                getSQLQuery(classDef, database).getTableName(),
                delete.getConditions()
        );
    }

    private static SQLQuery getSQLQuery(Class<?> classDef, SQLProvider database) {
        return database.getResolver().getSQLQuery(classDef);
    }
}
