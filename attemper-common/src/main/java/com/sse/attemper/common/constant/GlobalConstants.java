package com.sse.attemper.common.constant;

import java.util.Locale;

/**
 * 与项目、包名、配置文件名相关的常量
 * @auth ldang
 */
public interface GlobalConstants {

	String basePackageLocation = "com.sse.attemper.";
	
	String mybatisPlusMapperLocation = basePackageLocation + "**.dao.mapper*";
	
	String jpaRepositoryLocation = basePackageLocation + "**.dao.repo";

	String jpaEntityLocation = basePackageLocation + "**.entity";

	String LOG_DB_PREFIX = "";

	/**
	 * 默认服务名
	 */
	String defaultContextPath = "attemper";

	String statusPropertiesName = "status";

	Locale locale = Locale.getDefault();
}
