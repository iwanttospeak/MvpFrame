package com.app.baselib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author heping
 * 字段的标记类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldProp {
    boolean skip() default false;
    String keyName() default "";
    String requestKeyName() default "";
    String type() default "";
}
