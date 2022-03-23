package com.example.democommon.annotation;

import java.lang.annotation.*;


/**
 * 防止接口
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PreventSumbit {
    //String prefix() default "prefix:";
}
