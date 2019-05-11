package com.github.attemper.sys.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.sys.tag.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.resource.Resource;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.sys.service.TagService;
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
@Api("Tag")
@RestController
public class TagController {
	
	@Autowired
	private TagService service;

	@ApiOperation("List tags")
	@ApiImplicitParam(value = "TagListParam", name = "param", dataType = "TagListParam", required = true)
	@GetMapping(APIPath.TagPath.LIST)
	public CommonResult<Map<String, Object>> list(TagListParam param) {
		return CommonResult.putResult(service.list(param));
	}

	@ApiOperation("Get tag")
	@ApiImplicitParam(value = "TagGetParam", name = "param", dataType = "TagGetParam", required = true)
	@GetMapping(APIPath.TagPath.GET)
	public CommonResult<Tag> get(TagGetParam param) {
		return CommonResult.putResult(service.get(param));
	}

	@ApiOperation("Add tag")
	@ApiImplicitParam(value = "TagSaveParam", name = "param", dataType = "TagSaveParam", required = true)
	@PostMapping(APIPath.TagPath.ADD)
	public CommonResult<Tag> add(@RequestBody TagSaveParam param) {
		return CommonResult.putResult(service.add(param));
	}

	@ApiOperation("Update tag")
	@ApiImplicitParam(value = "TagSaveParam", name = "param", dataType = "TagSaveParam", required = true)
	@PutMapping(APIPath.TagPath.UPDATE)
	public CommonResult<Tag> update(@RequestBody TagSaveParam param) {
		return CommonResult.putResult(service.update(param));
	}

	@ApiOperation("Remove tag")
	@ApiImplicitParam(value = "TagRemoveParam", name = "param", dataType = "TagRemoveParam", required = true)
	@DeleteMapping(APIPath.TagPath.REMOVE)
	public CommonResult<Void> remove(@RequestBody TagRemoveParam param) {
		return CommonResult.putResult(service.remove(param));
	}

	@ApiOperation("Get users of tag")
	@ApiImplicitParam(value = "TagGetParam", name = "param", dataType = "TagGetParam", required = true)
	@GetMapping(APIPath.TagPath.TENANT_LIST)
	public CommonResult<List<Tenant>> getUsers(TagGetParam param) {
		return CommonResult.putResult(service.getTenants(param));
	}

	@ApiOperation("Update users of tag")
	@ApiImplicitParam(value = "TagTenantUpdateParam", name = "param", dataType = "TagTenantUpdateParam", required = true)
	@PutMapping(APIPath.TagPath.TENANT_UPDATE)
	public CommonResult<Void> updateTagTenants(@RequestBody TagTenantUpdateParam param) {
		return CommonResult.putResult(service.updateTagTenants(param));
    }

	@ApiOperation("Add resources of tag")
	@ApiImplicitParam(value = "TagGetParam", name = "param", dataType = "TagGetParam", required = true)
	@GetMapping(APIPath.TagPath.RESOURCE_LIST)
	public CommonResult<List<Resource>> getResources(TagGetParam param) {
		return CommonResult.putResult(service.getResources(param));
	}

	@ApiOperation("Update resources of tag")
	@ApiImplicitParam(value = "TagResourceUpdateParam", name = "param", dataType = "TagResourceUpdateParam", required = true)
	@PutMapping(APIPath.TagPath.RESOURCE_UPDATE)
	public CommonResult<Void> updateTagResources(@RequestBody TagResourceUpdateParam param) {
		return CommonResult.putResult(service.updateTagResources(param));
	}
}
