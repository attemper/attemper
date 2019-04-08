package com.sse.attemper.sys.controller;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.sys.tag.*;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.sys.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = APIConst.APITag.TAG)
@RestController
public class TagController {
	
	@Autowired
	private TagService service;

	@ApiOperation(APIConst.APIOperation.TagTitle.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "TagListParam", required = true)
	@GetMapping(APIPath.TagPath.LIST)
	public CommonResult list(TagListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(APIConst.APIOperation.TagTitle.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping(APIPath.TagPath.GET)
	public CommonResult get(TagGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(APIConst.APIOperation.TagTitle.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "TagSaveParam", required = true)
	@PostMapping(APIPath.TagPath.ADD)
	public CommonResult add(@RequestBody TagSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.TagTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TagSaveParam", required = true)
	@PutMapping(APIPath.TagPath.UPDATE)
	public CommonResult update(@RequestBody TagSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.TagTitle.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "TagRemoveParam", required = true)
	@DeleteMapping(APIPath.TagPath.REMOVE)
	public CommonResult remove(@RequestBody TagRemoveParam removeParam) {
		service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(APIConst.APIOperation.TagTitle.TAG_USER_GET)
    @ApiImplicitParam(value = "获取标签关联的用户数据", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping(APIPath.TagPath.USER_LIST)
	public CommonResult getUsers(TagGetParam getParam) {
		return CommonResult.putResult(service.getUsers(getParam));
	}

	@ApiOperation(APIConst.APIOperation.TagTitle.TAG_USER_UPDATE)
    @ApiImplicitParam(value = "保存标签关联的用户数据", name = "tagUserUpdateParam", dataType = "TagUserUpdateParam", required = true)
	@PutMapping(APIPath.TagPath.USER_UPDATE)
	public CommonResult updateTagUsers(@RequestBody TagUserUpdateParam tagUserUpdateParam) {
        service.updateTagUsers(tagUserUpdateParam);
		return CommonResult.ok();
    }

	@ApiOperation(APIConst.APIOperation.TagTitle.TAG_RESOURCE_GET)
	@ApiImplicitParam(value = "获取标签关联的资源数据", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping(APIPath.TagPath.RESOURCE_LIST)
	public CommonResult getResources(TagGetParam getParam) {
		return CommonResult.putResult(service.getResources(getParam));
	}

	@ApiOperation(APIConst.APIOperation.TagTitle.TAG_RESOURCE_UPDATE)
	@ApiImplicitParam(value = "保存标签关联的资源数据", name = "updateParam", dataType = "TagResourceUpdateParam", required = true)
	@PutMapping(APIPath.TagPath.RESOURCE_UPDATE)
	public CommonResult updateTagResources(@RequestBody TagResourceUpdateParam updateParam) {
		service.updateTagResources(updateParam);
		return CommonResult.ok();
	}
}
