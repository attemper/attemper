package com.sse.attemper.sys.controller;

import com.sse.attemper.common.constant.APIConst;
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
 * @auth ldang
 */
@Api(tags = APIConst.APITag.USER)
@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	/**
	 * 根据token获取用户信息
	 *
	 * @return
	 */
	@ApiOperation(APIConst.APIOperation.UserTitle.INFO)
	@GetMapping(APIPath.UserPath.INFO)
	public CommonResult<UserInfo> getUserInfo() {
		return CommonResult.putResult(service.getUserInfo());
	}

	@ApiOperation(APIConst.APIOperation.UserTitle.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "UserListParam", required = true)
	@GetMapping(APIPath.UserPath.LIST)
	public CommonResult<Map<String, Object>> list(UserListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(APIConst.APIOperation.UserTitle.GET)
	@ApiImplicitParam(value = "查询对象", name = "getParam", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.UserPath.GET)
	public CommonResult<User> get(UserGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(APIConst.APIOperation.UserTitle.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PostMapping(APIPath.UserPath.ADD)
	public CommonResult<User> add(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.UserTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PutMapping(APIPath.UserPath.UPDATE)
	public CommonResult<User> update(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.UserTitle.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "UserRemoveParam", required = true)
	@DeleteMapping(APIPath.UserPath.REMOVE)
	public CommonResult remove(@RequestBody UserRemoveParam removeParam) {
		service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(APIConst.APIOperation.UserTitle.USER_TAG_GET)
	@ApiImplicitParam(value = "获取用户关联的标签数据", name = "getParam", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.UserPath.TAG_LIST)
	public CommonResult<List<Tag>> getTags(UserGetParam getParam) {
		return CommonResult.putResult(service.getTags(getParam));
	}

	@ApiOperation(APIConst.APIOperation.UserTitle.USER_TAG_UPDATE)
	@ApiImplicitParam(value = "用户关联的标签数据", name = "updateParam", dataType = "UserTagUpdateParam", required = true)
	@PutMapping(APIPath.UserPath.TAG_UPDATE)
	public CommonResult updateUserTags(@RequestBody UserTagUpdateParam updateParam) {
		service.updateUserTags(updateParam);
		return CommonResult.ok();
	}
}
