package com.minfo.carrepair.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindViewById {

    int DEFAULT_ID = -1;

    int id() default DEFAULT_ID;

    String DEFAULT_METHOD = "";

    String click() default DEFAULT_METHOD;
}
