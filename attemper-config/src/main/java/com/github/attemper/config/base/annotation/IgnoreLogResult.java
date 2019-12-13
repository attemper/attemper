package com.github.attemper.config.base.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLogResult {

	boolean value() default true;
}
