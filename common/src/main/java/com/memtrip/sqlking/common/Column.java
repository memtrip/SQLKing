package com.memtrip.sqlking.common;

public @interface Column {
    boolean index() default false;
    ForeignKey foreign_key() default @ForeignKey(hasForeignKey = false);
}