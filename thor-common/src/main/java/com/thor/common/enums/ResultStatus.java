package com.thor.common.enums;

/**
 * 状态码
 * @author ldang
 */
public enum ResultStatus {

	/* 200:调用成功 */
	OK(200),
	
	/* 500:服务器内部执行报错（空指针异常、sql执行报错等）*/
	INTERNAL_SERVER_ERROR(500),
	
	
	/* 1000-1199 安全认证报错 */
	SECURITY_INVALID_TOKEN(1000),

	JWT_INCORRECT_EXCEPTION(1001),

	NO_ACCESS_TOKEN_EXCEPTION(1002),
	
	/* 1200-1299 SQL报错*/
	SQL_ERROR_DUMPLICATE_KEY(1200),

	/* 1300-1499 用户相关 */
	USER_USERNAME_OR_PASSWORD_ERROR(1300),

	USER_DUPLICATED_ERROR(1301),

	USER_FROZEN_ERROR(1302),

	USER_DELETED_ERROR(1303),

	USER_NOTEXIST_ERROR(1303),

	/* 1500-1599 租户相关*/
	TENANT_NOTEXIST_ERROR(1500),

	TENANT_SIGN_INCORRECT_ERROR(1501),


	/* 5000-?各接口报错 */
	/* 登录接口5000-5099 */

	/* 租户接口5100-5199 */
	INTERFACE_TENANT_UPDATE_NOTEXIST_ERROR(5150),

	/* 用户接口5200-5299*/
	INTERFACE_USER_UPDATE_NOTEXIST_ERROR(5250),

	/* 标签接口5300-5399*/
	INTERFACE_TAG_UPDATE_NOTEXIST_ERROR(5350),

    /* 资源接口5400-5499 */
    INTERFACE_RESOURCE_NOT_ONE_ROOT_ERROR(5470),
	;
	
	private int code;

	ResultStatus(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
