package com.thor.sys.service;

import com.stark.sdk.common.result.CommonResult;
import com.stark.sdk.common.result.user.UserInfo;
import com.stark.sdk.microservice.client.StarkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ldang
 */
@Service
public class UserService extends BaseServiceAdapter {

	/**
	 * @see com.thor.config.conf.StarkClientConfig
	 */
	@Autowired
	private StarkClient starkClient;

	/**
	 * 根据token获取用户信息
	 *
	 * @return
	 */
	public CommonResult<UserInfo> getUserInfo() {
		return starkClient.getUserInfo();
	}

}
