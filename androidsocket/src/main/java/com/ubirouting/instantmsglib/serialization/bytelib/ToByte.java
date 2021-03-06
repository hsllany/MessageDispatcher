package com.ubirouting.instantmsglib.serialization.bytelib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToByte {

    int LAST = Integer.MAX_VALUE;

    int FIRST = Integer.MIN_VALUE;

    int order() default -1;

    String description() default "null";
}
