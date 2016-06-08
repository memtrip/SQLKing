package com.memtrip.sqlking.database;

import android.database.Cursor;

import com.memtrip.sqlking.common.SQLQuery;
import com.memtrip.sqlking.operation.function.Count;
import com.memtrip.sqlking.operation.function.Delete;
import com.memtrip.sqlking.operation.function.Insert;
import com.memtrip.sqlking.operation.function.Select;
import com.memtrip.sqlking.operation.function.Update;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;

public abstract class Query {

    protected static void insert(Insert insert, Class<?> classDef, SQLProvider sqlProvider) {
        if (insert.getModels() != null && insert.getModels().length > 0) {
            String[] unionInsert = getSQLQuery(classDef,sqlProvider).buildUnionInsertQuery(insert.getModels());
            sqlProvider.insertMultiple(unionInsert);
        }
    }

    protected static Cursor selectCursor(Select select, Class<?> classDef, SQLProvider sqlProvider) {

        SQLQuery sqlQuery = getSQLQuery(classDef, sqlProvider);

        return sqlProvider.query(
                sqlQuery.getTableName(),
                sqlQuery.getColumnNames(),
                select.getClause(),
                select.getJoin(),
                null,
                null,
                select.getOrderBy(),
                select.getLimit()
        );
    }

    protected static <T> T[] select(Select select, Class<?> classDef, SQLProvider sqlProvider) {
        Cursor cursor = selectCursor(select, classDef, sqlProvider);
        return getSQLQuery(classDef, sqlProvider).retrieveSQLSelectResults(cursor);
    }

    protected static <T> T selectSingle(Select select, Class<?> classDef, SQLProvider sqlProvider) {
        Cursor cursor = selectCursor(select, classDef, sqlProvider);

        T[] results = getSQLQuery(classDef, sqlProvider).retrieveSQLSelectResults(cursor);

        if (results != null && results.length > 0) {
            return results[0];
        } else {
            return null;
        }
    }

    protected static int update(Update update, Class<?> classDef, SQLProvider sqlProvider) {
        return sqlProvider.update(
                getSQLQuery(classDef, sqlProvider).getTableName(),
                update.getContentValues(),
                update.getConditions()
        );
    }

    protected static long count(Count count, Class<?> classDef, SQLProvider sqlProvider) {
        return sqlProvider.count(
                getSQLQuery(classDef, sqlProvider).getTableName(),
                count.getClause()
        );
    }

    protected static int delete(Delete delete, Class<?> classDef, SQLProvider sqlProvider) {
        return sqlProvider.delete(
                getSQLQuery(classDef, sqlProvider).getTableName(),
                delete.getConditions()
        );
    }

    protected static Cursor rawQuery(String query, SQLProvider sqlProvider) {
        return sqlProvider.rawQuery(query);
    }

    protected static <T> Observable<T> wrapRx(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                }
        );
    }

    private static SQLQuery getSQLQuery(Class<?> classDef, SQLProvider sqlProvider) {
        return sqlProvider.getResolver().getSQLQuery(classDef);
    }
}
