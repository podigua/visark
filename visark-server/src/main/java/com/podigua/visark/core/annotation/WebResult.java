package com.podigua.visark.core.annotation;

import java.lang.annotation.*;

/**
 * @author podigua
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebResult {

    boolean value() default true;
}
