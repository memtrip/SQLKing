package com.memtrip.sqlking.common;

public @interface Member {
    boolean index() default false;
    String foreign_key() default "";
}