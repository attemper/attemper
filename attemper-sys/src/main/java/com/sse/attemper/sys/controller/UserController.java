package com.sse.attemper.sys.controller;

import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.sys.user.*;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.sys.tag.Tag;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.common.result.sys.user.UserInfo;
import com.sse.attemper.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Api("User")
@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	/**
	 * get user by token
	 *
	 * @return
	 */
	@ApiOperation("Get user&resource&tag by token")
	@GetMapping(APIPath.UserPath.INFO)
	public CommonResult<UserInfo> getUserInfo() {
		return CommonResult.putResult(service.getUserInfo());
	}

	@ApiOperation("List users")
	@ApiImplicitParam(value = "UserListParam", name = "param", dataType = "UserListParam", required = true)
	@GetMapping(APIPath.UserPath.LIST)
	public CommonResult<Map<String, Object>> list(UserListParam param) {
		return CommonResult.putResult(service.list(param));
	}

	@ApiOperation("Get user")
	@ApiImplicitParam(value = "UserGetParam", name = "param", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.UserPath.GET)
	public CommonResult<User> get(UserGetParam param) {
		return CommonResult.putResult(service.get(param));
	}

	@ApiOperation("Add user")
	@ApiImplicitParam(value = "UserSaveParam", name = "param", dataType = "UserSaveParam", required = true)
	@PostMapping(APIPath.UserPath.ADD)
	public CommonResult<User> add(@RequestBody UserSaveParam param) {
		return CommonResult.putResult(service.add(param));
	}

	@ApiOperation("Update user")
	@ApiImplicitParam(value = "UserSaveParam", name = "param", dataType = "UserSaveParam", required = true)
	@PutMapping(APIPath.UserPath.UPDATE)
	public CommonResult<User> update(@RequestBody UserSaveParam param) {
		return CommonResult.putResult(service.update(param));
	}

	@ApiOperation("Remove users")
	@ApiImplicitParam(value = "UserRemoveParam", name = "param", dataType = "UserRemoveParam", required = true)
	@DeleteMapping(APIPath.UserPath.REMOVE)
	public CommonResult<Void> remove(@RequestBody UserRemoveParam param) {
		return CommonResult.putResult(service.remove(param));
	}

	@ApiOperation("Get tag by user")
	@ApiImplicitParam(value = "UserGetParam", name = "param", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.UserPath.TAG_LIST)
	public CommonResult<List<Tag>> getTags(UserGetParam param) {
		return CommonResult.putResult(service.getTags(param));
	}

	@ApiOperation("Update tag by user")
	@ApiImplicitParam(value = "UserTagUpdateParam", name = "param", dataType = "UserTagUpdateParam", required = true)
	@PutMapping(APIPath.UserPath.TAG_UPDATE)
	public CommonResult updateUserTags(@RequestBody UserTagUpdateParam param) {
		service.updateUserTags(param);
		return CommonResult.ok();
	}
}
