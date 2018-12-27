package com.thor.config.constant;

import com.thor.common.constant.GlobalConstants;

/**
 * 使用表生成主键的常量管理
 * @auth ldang
 */
public interface PrimaryKeyGeneratorConstants {

    String GENERATOR_TABLE = GlobalConstants.TABLE_PREFIX + "pk_generator";

    String PK_COLUMN_NAME = "TABLE_NAME";

    String PK_COLUMN_VALUE = "CURR_VALUE";

}
