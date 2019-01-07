package com.thor.common.constant;

/**
 * 与项目、包名、配置文件名相关的常量
 * @auth ldang
 */
public interface GlobalConstants {

	String basePackageLocation = "com.thor.";
	
	String mybatisPlusMapperLocation = basePackageLocation + "**.dao.mapper*";
	
	String jpaRepositoryLocation = basePackageLocation + "**.dao.repo";

	String jpaEntityLocation = basePackageLocation + "**.entity";

	String LOG_DB_PREFIX = "";

	String TABLE_PREFIX = "stark_";

}
