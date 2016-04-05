package com.memtrip.sqlking.common;

public @interface Column {
    boolean index() default false;
    String foreign_key() default "";
}