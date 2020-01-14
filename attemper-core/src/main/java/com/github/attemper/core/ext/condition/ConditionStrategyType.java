package com.github.attemper.core.ext.condition;

import com.github.attemper.common.enums.ConditionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionStrategyType {
    ConditionType value() default ConditionType.FTP_FILE;
}
