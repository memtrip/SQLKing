package com.memtrip.sqlking.common;

public interface Resolver {
    SQLQuery getSQLQuery(Class<?> classDef);
}