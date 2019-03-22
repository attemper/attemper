package com.sse.attemper.sys.annotation;

import java.lang.annotation.*;

/**
 * 因为不使用本项目剥离权限认证给多租户服务，因此使用此注解来过滤掉登录等无须校验token的
 * 是否忽略认证
 * @author ldang
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
