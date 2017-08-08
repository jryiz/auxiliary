package com.auxiliary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ucmed on 2017/6/14.
 * 一个注释类，方便通过反射去转换对象
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface FieldValue {
   public String value() default "";
}
