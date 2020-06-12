package com.shh.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    /**
     * 是否自动生成
     *
     * @return autoGenerate
     */
    boolean autoGenerate() default false;

    /**
     * keyType == 0 auto increment number
     * <p>
     * keyType == 1 UUID
     *
     * @return keyType
     */
    int keyType() default 0;
}
