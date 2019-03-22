package com.sse.attemper.common.constant;

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

}
