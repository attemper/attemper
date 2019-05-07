package com.github.attemper.common.constant;

import java.util.Locale;

/**
 * 与项目、包名、配置文件名相关的常量
 * @auth ldang
 */
public interface GlobalConstants {

	String defaultContextPath = "attemper";

	String basePackageLocation = "com.github." + defaultContextPath + ".";

	String mybatisPlusMapperLocation = basePackageLocation + "**.dao.mapper*";

	//String jpaRepositoryLocation = basePackageLocation + "**.dao.repo";

	//String jpaEntityLocation = basePackageLocation + "**.entity";

	//String LOG_DB_PREFIX = "";

	String statusPropertiesName = "status";

	Locale locale = Locale.getDefault();
}
