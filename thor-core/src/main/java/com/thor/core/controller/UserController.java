package com.thor.core.controller;

import com.thor.common.constant.APIPath;
import com.thor.common.result.CommonResult;
import com.thor.config.constant.APIConst;
import com.thor.core.param.user.UserGetParam;
import com.thor.core.param.user.UserListParam;
import com.thor.core.param.user.UserRemoveParam;
import com.thor.core.param.user.UserSaveParam;
import com.thor.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = APIConst.Tag.USER)
@RestController
@RequestMapping(APIPath.Sys.USER)
public class UserController {
	
	@Autowired
	private UserService service;

	@ApiOperation(APIConst.Operation.User.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "UserListParam", required = true)
	@GetMapping(APIPath.LIST)
	public CommonResult list(UserListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(APIConst.Operation.User.GET)
    @ApiImplicitParam(value = "查询对象", name = "getParam", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.GET)
	public CommonResult get(UserGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(APIConst.Operation.User.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PostMapping(APIPath.ADD)
	public CommonResult add(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(APIConst.Operation.User.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "UserSaveParam", required = true)
	@PutMapping(APIPath.UPDATE)
	public CommonResult update(@RequestBody UserSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(APIConst.Operation.User.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "UserRemoveParam", required = true)
	@DeleteMapping(APIPath.REMOVE)
	public CommonResult remove(@RequestBody UserRemoveParam removeParam){
		service.remove(removeParam);
		return CommonResult.ok();
	}
}
