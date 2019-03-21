package com.thor.sys.controller;

import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.sys.user.*;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sdk.common.result.sys.tag.Tag;
import com.thor.sdk.common.result.sys.user.User;
import com.thor.sdk.common.result.sys.user.UserInfo;
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
@Api(tags = ThorAPIConst.APITag.USER)
@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	/**
	 * 根据token获取用户信息
	 *
	 * @return
	 */
	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.INFO)
	@GetMapping(ThorAPIPath.UserPath.INFO)
	public CommonResult<UserInfo> getUserInfo() {
		return CommonResult.putResult(service.getUserInfo());
	}

	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "UserListParam", required = true)
	@GetMapping(ThorAPIPath.UserPath.LIST)
	public CommonResult<Map<String, Object>> list(UserListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.GET)
	@ApiImplicitParam(value = "查询对象", name = "getParam", dataType = "UserGetParam", required = true)
	@GetMapping(ThorAPIPath.UserPath.GET)
	public CommonResult<User> get(UserGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PostMapping(ThorAPIPath.UserPath.ADD)
	public CommonResult<User> add(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PutMapping(ThorAPIPath.UserPath.UPDATE)
	public CommonResult<User> update(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "UserRemoveParam", required = true)
	@DeleteMapping(ThorAPIPath.UserPath.REMOVE)
	public CommonResult remove(@RequestBody UserRemoveParam removeParam) {
		service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.USER_TAG_GET)
	@ApiImplicitParam(value = "获取用户关联的标签数据", name = "getParam", dataType = "UserGetParam", required = true)
	@GetMapping(ThorAPIPath.UserPath.TAG_LIST)
	public CommonResult<List<Tag>> getTags(UserGetParam getParam) {
		return CommonResult.putResult(service.getTags(getParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.UserTitle.USER_TAG_UPDATE)
	@ApiImplicitParam(value = "用户关联的标签数据", name = "updateParam", dataType = "UserTagUpdateParam", required = true)
	@PutMapping(ThorAPIPath.UserPath.TAG_UPDATE)
	public CommonResult updateUserTags(@RequestBody UserTagUpdateParam updateParam) {
		service.updateUserTags(updateParam);
		return CommonResult.ok();
	}
}
