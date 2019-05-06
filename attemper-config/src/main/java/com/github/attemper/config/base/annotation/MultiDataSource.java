package com.github.attemper.config.base.annotation;

import com.github.attemper.config.base.datasource.DataSourceName;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * @author ldang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiDataSource {
	
	/** 当标记上这个注释后，默认切到第二个数据源 */
    String value() default DataSourceName.SECOND;
}
