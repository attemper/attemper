package com.github.attemper.config.base.annotation;

import com.github.attemper.config.base.datasource.DataSourceName;

import java.lang.annotation.*;

/**
 * multi datasource annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiDataSource {
	
	/** change to second */
    String value() default DataSourceName.SECOND;
}
