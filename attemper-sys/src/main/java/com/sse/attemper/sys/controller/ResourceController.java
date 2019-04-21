package com.sse.attemper.sys.controller;

import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.EmptyParam;
import com.sse.attemper.common.param.sys.resource.ResourceRemoveParam;
import com.sse.attemper.common.param.sys.resource.ResourceSaveParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.sys.resource.Resource;
import com.sse.attemper.sys.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ldang
 */
@Api("Resource")
@RestController
public class ResourceController {
	
	@Autowired
	private ResourceService service;

	@ApiOperation("List resource tree")
	@ApiImplicitParam(value = "EmptyParam", name = "param", dataType = "EmptyParam", required = true)
	@GetMapping(APIPath.ResourcePath.TREE_LIST)
	public CommonResult<List<Resource>> getTreeList(EmptyParam param) {
		return CommonResult.putResult(service.getAll(param));
	}

	@ApiOperation("Add or update resource")
	@ApiImplicitParam(value = "ResourceSaveParam", name = "param", dataType = "ResourceSaveParam", required = true)
	@PostMapping(APIPath.ResourcePath.SAVE)
	public CommonResult<Resource> add(@RequestBody ResourceSaveParam param) {
		return CommonResult.putResult(service.save(param));
	}

	@ApiOperation("Remove resources")
	@ApiImplicitParam(value = "ResourceRemoveParam", name = "param", dataType = "ResourceRemoveParam", required = true)
	@DeleteMapping(APIPath.ResourcePath.REMOVE)
	public CommonResult<Void> remove(@RequestBody ResourceRemoveParam param) {
		return CommonResult.putResult(service.remove(param));
    }
}
