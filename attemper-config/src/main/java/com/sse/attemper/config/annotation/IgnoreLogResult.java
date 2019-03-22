package com.sse.attemper.config.annotation;

import java.lang.annotation.*;

/**
 * 方法返回值是否需要记录<br>
 * 如果返回值含列表，意味着很有可能数据量很大，根据需要记录<br>
 * @author ldang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLogResult {

	/**
	 * 默认不记录结果
	 * @return
	 */
	boolean value() default true;
}
