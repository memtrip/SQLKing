package com.memtrip.sqlking.common;

public @interface ForeignKey {
    boolean hasForeignKey() default true;
    String thisColumn() default "";
    String foreignColumn() default "";
}