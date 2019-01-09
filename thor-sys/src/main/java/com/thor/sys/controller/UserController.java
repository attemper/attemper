package com.thor.sys.controller;

import com.stark.sdk.common.constant.StarkAPIConst;
import com.stark.sdk.common.constant.StarkAPIPath;
import com.stark.sdk.common.result.CommonResult;
import com.stark.sdk.common.result.user.UserInfo;
import com.thor.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @auth ldang
 */
@Api(tags = StarkAPIConst.Tag.USER)
@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	/**
	 * 根据token获取用户信息
	 *
	 * @return
	 */
	@ApiOperation(StarkAPIConst.Operation.User.INFO)
	@GetMapping(StarkAPIPath.User.INFO)
	public CommonResult<UserInfo> getUserInfo() {
		return service.getUserInfo();
	}

}
