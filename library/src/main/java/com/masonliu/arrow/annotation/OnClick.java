package com.masonliu.arrow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by liumeng on 16/11/20.
 */
@Retention(RUNTIME)
@Target({ElementType.METHOD})
public @interface OnClick {
    int value();
}
