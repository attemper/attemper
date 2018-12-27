package com.thor.core.annotation;

import java.lang.annotation.*;

/**
 * @author ldang
 * 是否忽略认证
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuthentication {

    /**
     * 默认跳过认证<br>
     * @return <code>true</code> as default value
     */
    boolean value() default true;

}
