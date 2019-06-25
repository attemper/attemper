package com.github.attemper.common.constant;

public interface GlobalConstants {

	String defaultContextPath = "attemper";

	String basePackageLocation = "com.github." + defaultContextPath + ".";

	String mybatisPlusMapperLocation = basePackageLocation + "**.dao.mapper*";

	String statusPropertiesName = "status";

}
