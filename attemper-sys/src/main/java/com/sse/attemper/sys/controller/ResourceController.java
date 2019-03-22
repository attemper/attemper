package com.sse.attemper.sys.controller;

import com.sse.attemper.sdk.common.constant.APIConst;
import com.sse.attemper.sdk.common.constant.APIPath;
import com.sse.attemper.sdk.common.param.EmptyParam;
import com.sse.attemper.sdk.common.param.sys.resource.ResourceRemoveParam;
import com.sse.attemper.sdk.common.param.sys.resource.ResourceSaveParam;
import com.sse.attemper.sdk.common.result.CommonResult;
import com.sse.attemper.sys.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = APIConst.APITag.RESOURCE)
@RestController
public class ResourceController {
	
	@Autowired
	private ResourceService service;

	@ApiOperation(APIConst.APIOperation.ResourceTitle.TREE_LIST)
	@ApiImplicitParam(value = "查询资源树", name = "emptyParam", dataType = "EmptyParam", required = true)
	@GetMapping(APIPath.ResourcePath.TREE_LIST)
    public CommonResult getTreeList(EmptyParam emptyParam) {
        return CommonResult.putResult(service.getAll(emptyParam));
	}

	@ApiOperation(APIConst.APIOperation.ResourceTitle.SAVE)
	@ApiImplicitParam(value = "被保存的数据", name = "saveParam", dataType = "ResourceSaveParam", required = true)
	@PostMapping(APIPath.ResourcePath.SAVE)
    public CommonResult add(@RequestBody ResourceSaveParam saveParam) {
        return CommonResult.putResult(service.save(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.ResourceTitle.REMOVE)
    @ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "ResourceRemoveParam", required = true)
	@DeleteMapping(APIPath.ResourcePath.REMOVE)
    public CommonResult remove(@RequestBody ResourceRemoveParam removeParam) {
        service.remove(removeParam);
        return CommonResult.ok();
    }
}
