package com.thor.sys.controller;

import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.user.*;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sdk.common.result.tag.Tag;
import com.thor.sdk.common.result.user.User;
import com.thor.sdk.common.result.user.UserInfo;
import com.thor.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 
 * @auth ldang
 */
@Api(tags = ThorAPIConst.Tag.USER)
@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	/**
	 * 根据token获取用户信息
	 *
	 * @return
	 */
	@ApiOperation(ThorAPIConst.Operation.User.INFO)
	@GetMapping(ThorAPIPath.User.INFO)
	public CommonResult<UserInfo> getUserInfo() {
		return CommonResult.putResult(service.getUserInfo());
	}

	@ApiOperation(ThorAPIConst.Operation.User.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "UserListParam", required = true)
	@GetMapping(ThorAPIPath.User.LIST)
	public CommonResult<Map<String, Object>> list(UserListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(ThorAPIConst.Operation.User.GET)
	@ApiImplicitParam(value = "查询对象", name = "getParam", dataType = "UserGetParam", required = true)
	@GetMapping(ThorAPIPath.User.GET)
	public CommonResult<User> get(UserGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(ThorAPIConst.Operation.User.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PostMapping(ThorAPIPath.User.ADD)
	public CommonResult<User> add(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(ThorAPIConst.Operation.User.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PutMapping(ThorAPIPath.User.UPDATE)
	public CommonResult<User> update(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(ThorAPIConst.Operation.User.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "UserRemoveParam", required = true)
	@DeleteMapping(ThorAPIPath.User.REMOVE)
	public CommonResult remove(@RequestBody UserRemoveParam removeParam) {
		service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(ThorAPIConst.Operation.User.USER_TAG_GET)
	@ApiImplicitParam(value = "获取用户关联的标签数据", name = "getParam", dataType = "UserGetParam", required = true)
	@GetMapping(ThorAPIPath.User.TAG_LIST)
	public CommonResult<List<Tag>> getTags(UserGetParam getParam) {
		return CommonResult.putResult(service.getTags(getParam));
	}

	@ApiOperation(ThorAPIConst.Operation.User.USER_TAG_UPDATE)
	@ApiImplicitParam(value = "用户关联的标签数据", name = "updateParam", dataType = "UserTagUpdateParam", required = true)
	@PutMapping(ThorAPIPath.User.TAG_UPDATE)
	public CommonResult updateUserTags(@RequestBody UserTagUpdateParam updateParam) {
		service.updateUserTags(updateParam);
		return CommonResult.ok();
	}
}
