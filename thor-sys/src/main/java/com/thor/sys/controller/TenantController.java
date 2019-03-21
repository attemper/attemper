package com.thor.sys.controller;

import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.sys.tenant.TenantGetParam;
import com.thor.sdk.common.param.sys.tenant.TenantListParam;
import com.thor.sdk.common.param.sys.tenant.TenantRemoveParam;
import com.thor.sdk.common.param.sys.tenant.TenantSaveParam;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sys.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 
 * @auth ldang
 */
@Api(tags = ThorAPIConst.APITag.TENANT)
@RestController
public class TenantController {
	
	@Autowired
	private TenantService service;

	@ApiOperation(ThorAPIConst.APIOperation.TenantTitle.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PostMapping(ThorAPIPath.TenantPath.ADD)
	public CommonResult add(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.TenantTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PutMapping(ThorAPIPath.TenantPath.UPDATE)
	public CommonResult update(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.TenantTitle.LIST)
    @ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "TenantListParam", required = true)
	@GetMapping(ThorAPIPath.TenantPath.LIST)
	public CommonResult list(TenantListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.TenantTitle.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "TenantRemoveParam", required = true)
	@DeleteMapping(ThorAPIPath.TenantPath.REMOVE)
	public CommonResult remove(@RequestBody TenantRemoveParam removeParam) {
        service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(ThorAPIConst.APIOperation.TenantTitle.GET)
    @ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TenantGetParam", required = true)
	@GetMapping(ThorAPIPath.TenantPath.GET)
	public CommonResult get(TenantGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
    }

}
