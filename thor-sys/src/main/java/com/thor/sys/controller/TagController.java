package com.thor.sys.controller;

import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.tag.*;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sys.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = ThorAPIConst.Tag.TAG)
@RestController
public class TagController {
	
	@Autowired
	private TagService service;

	@ApiOperation(ThorAPIConst.Operation.Tag.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "TagListParam", required = true)
	@GetMapping(ThorAPIPath.Tag.LIST)
	public CommonResult list(TagListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Tag.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping(ThorAPIPath.Tag.GET)
	public CommonResult get(TagGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Tag.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "TagSaveParam", required = true)
	@PostMapping(ThorAPIPath.Tag.ADD)
	public CommonResult add(@RequestBody TagSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Tag.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TagSaveParam", required = true)
	@PutMapping(ThorAPIPath.Tag.UPDATE)
	public CommonResult update(@RequestBody TagSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Tag.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "TagRemoveParam", required = true)
	@DeleteMapping(ThorAPIPath.Tag.REMOVE)
	public CommonResult remove(@RequestBody TagRemoveParam removeParam) {
		service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(ThorAPIConst.Operation.Tag.TAG_USER_GET)
    @ApiImplicitParam(value = "获取标签关联的用户数据", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping(ThorAPIPath.Tag.USER_LIST)
	public CommonResult getUsers(TagGetParam getParam) {
		return CommonResult.putResult(service.getUsers(getParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Tag.TAG_USER_UPDATE)
    @ApiImplicitParam(value = "保存标签关联的用户数据", name = "tagUserUpdateParam", dataType = "TagUserUpdateParam", required = true)
	@PutMapping(ThorAPIPath.Tag.USER_UPDATE)
	public CommonResult updateTagUsers(@RequestBody TagUserUpdateParam tagUserUpdateParam) {
        service.updateTagUsers(tagUserUpdateParam);
		return CommonResult.ok();
    }

	@ApiOperation(ThorAPIConst.Operation.Tag.TAG_RESOURCE_GET)
	@ApiImplicitParam(value = "获取标签关联的资源数据", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping(ThorAPIPath.Tag.RESOURCE_LIST)
	public CommonResult getResources(TagGetParam getParam) {
		return CommonResult.putResult(service.getResources(getParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Tag.TAG_RESOURCE_UPDATE)
	@ApiImplicitParam(value = "保存标签关联的资源数据", name = "updateParam", dataType = "TagResourceUpdateParam", required = true)
	@PutMapping(ThorAPIPath.Tag.RESOURCE_UPDATE)
	public CommonResult updateTagResources(@RequestBody TagResourceUpdateParam updateParam) {
		service.updateTagResources(updateParam);
		return CommonResult.ok();
	}
}
