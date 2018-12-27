package com.thor.core.controller;

import com.thor.common.constant.APIPath;
import com.thor.common.result.CommonResult;
import com.thor.config.constant.APIConst;
import com.thor.core.param.EmptyParam;
import com.thor.core.param.resource.ResourceRemoveParam;
import com.thor.core.param.resource.ResourceSaveParam;
import com.thor.core.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = APIConst.Tag.RESOURCE)
@RestController
@RequestMapping(APIPath.Sys.RESOURCE)
public class ResourceController {
	
	@Autowired
	private ResourceService service;

	@ApiOperation(APIConst.Operation.Resource.TREE_LIST)
	@ApiImplicitParam(value = "查询资源树", name = "emptyParam", dataType = "EmptyParam", required = true)
	@GetMapping(APIPath.Sys.Resource.TREE_LIST)
	public CommonResult getTreeList(EmptyParam emptyParam) {
		return CommonResult.putResult(service.getAll(emptyParam));
	}

	@ApiOperation(APIConst.Operation.Resource.SAVE)
	@ApiImplicitParam(value = "被保存的数据", name = "saveParam", dataType = "ResourceSaveParam", required = true)
	@PostMapping(APIPath.SAVE)
	public CommonResult add(@RequestBody ResourceSaveParam saveParam) {
		return CommonResult.putResult(service.save(saveParam));
	}

    @ApiOperation(APIConst.Operation.Resource.REMOVE)
    @ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "ResourceRemoveParam", required = true)
    @DeleteMapping(APIPath.REMOVE)
    public CommonResult remove(@RequestBody ResourceRemoveParam removeParam){
        service.remove(removeParam);
        return CommonResult.ok();
    }
}
