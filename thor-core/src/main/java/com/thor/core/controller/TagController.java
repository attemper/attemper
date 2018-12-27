package com.thor.core.controller;

import com.thor.common.constant.APIPath;
import com.thor.common.result.CommonResult;
import com.thor.config.constant.APIConst;
import com.thor.core.param.tag.*;
import com.thor.core.service.TagService;
import com.thor.core.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = APIConst.Tag.TAG)
@RestController
@RequestMapping(APIPath.Sys.TAG)
public class TagController {
	
	@Autowired
	private TagService service;

	@ApiOperation(APIConst.Operation.Tag.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "TagListParam", required = true)
	@GetMapping(APIPath.LIST)
	public CommonResult list(TagListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(APIConst.Operation.Tag.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping(APIPath.GET)
	public CommonResult get(TagGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(APIConst.Operation.Tag.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "TagSaveParam", required = true)
	@PostMapping(APIPath.ADD)
	public CommonResult add(@RequestBody TagSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(APIConst.Operation.Tag.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TagSaveParam", required = true)
	@PutMapping(APIPath.UPDATE)
	public CommonResult update(@RequestBody TagSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(APIConst.Operation.Tag.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "TagRemoveParam", required = true)
	@DeleteMapping(APIPath.REMOVE)
	public CommonResult remove(@RequestBody TagRemoveParam removeParam){
		service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(APIConst.Operation.Tag.TAG_USER_GET)
    @ApiImplicitParam(value = "获取标签关联的用户数据", name = "getParam", dataType = "TagGetParam", required = true)
    @GetMapping("user")
	public CommonResult getTagUsers(TagGetParam getParam) {
        return CommonResult.putResult(service.getTagUsers(getParam));
	}

    @ApiOperation(APIConst.Operation.Tag.TAG_USER_UPDATE)
    @ApiImplicitParam(value = "保存标签关联的用户数据", name = "tagUserUpdateParam", dataType = "TagUserUpdateParam", required = true)
    @PutMapping("user")
    public CommonResult updateTagUsers(@RequestBody TagUserUpdateParam tagUserUpdateParam) {
        service.updateTagUsers(tagUserUpdateParam);
        return CommonResult.ok();
    }

	@ApiOperation(APIConst.Operation.Tag.TAG_RESOURCE_GET)
	@ApiImplicitParam(value = "获取标签关联的资源数据", name = "getParam", dataType = "TagGetParam", required = true)
	@GetMapping("resource")
	public CommonResult getTagResources(TagGetParam getParam) {
		return CommonResult.putResult(service.getTagResources(getParam));
	}

	@ApiOperation(APIConst.Operation.Tag.TAG_RESOURCE_UPDATE)
	@ApiImplicitParam(value = "保存标签关联的资源数据", name = "updateParam", dataType = "TagResourceUpdateParam", required = true)
	@PutMapping("resource")
	public CommonResult updateTagResources(@RequestBody TagResourceUpdateParam updateParam) {
		service.updateTagResources(updateParam);
		return CommonResult.ok();
	}
}
