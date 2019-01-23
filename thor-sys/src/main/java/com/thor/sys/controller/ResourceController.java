package com.thor.sys.controller;

import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.EmptyParam;
import com.thor.sdk.common.param.resource.ResourceRemoveParam;
import com.thor.sdk.common.param.resource.ResourceSaveParam;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sys.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = ThorAPIConst.Tag.RESOURCE)
@RestController
public class ResourceController {
	
	@Autowired
	private ResourceService service;

	@ApiOperation(ThorAPIConst.Operation.Resource.TREE_LIST)
	@ApiImplicitParam(value = "查询资源树", name = "emptyParam", dataType = "EmptyParam", required = true)
	@GetMapping(ThorAPIPath.Resource.TREE_LIST)
    public CommonResult getTreeList(EmptyParam emptyParam) {
        return CommonResult.putResult(service.getAll(emptyParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Resource.SAVE)
	@ApiImplicitParam(value = "被保存的数据", name = "saveParam", dataType = "ResourceSaveParam", required = true)
	@PostMapping(ThorAPIPath.Resource.SAVE)
    public CommonResult add(@RequestBody ResourceSaveParam saveParam) {
        return CommonResult.putResult(service.save(saveParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Resource.REMOVE)
    @ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "ResourceRemoveParam", required = true)
	@DeleteMapping(ThorAPIPath.Resource.REMOVE)
    public CommonResult remove(@RequestBody ResourceRemoveParam removeParam) {
        service.remove(removeParam);
        return CommonResult.ok();
    }
}
