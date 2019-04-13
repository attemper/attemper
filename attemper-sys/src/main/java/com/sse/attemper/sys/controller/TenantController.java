package com.sse.attemper.sys.controller;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.sys.tenant.TenantGetParam;
import com.sse.attemper.common.param.sys.tenant.TenantListParam;
import com.sse.attemper.common.param.sys.tenant.TenantRemoveParam;
import com.sse.attemper.common.param.sys.tenant.TenantSaveParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.sys.tenant.Tenant;
import com.sse.attemper.sys.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 
 * @auth ldang
 */
@Api(tags = APIConst.APITag.TENANT)
@RestController
public class TenantController {
	
	@Autowired
	private TenantService service;

	@ApiOperation(APIConst.APIOperation.TenantTitle.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PostMapping(APIPath.TenantPath.ADD)
	public CommonResult<Tenant> add(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.TenantTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PutMapping(APIPath.TenantPath.UPDATE)
	public CommonResult<Tenant> update(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.TenantTitle.LIST)
    @ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "TenantListParam", required = true)
	@GetMapping(APIPath.TenantPath.LIST)
	public CommonResult<Map<String, Object>> list(TenantListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(APIConst.APIOperation.TenantTitle.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "TenantRemoveParam", required = true)
	@DeleteMapping(APIPath.TenantPath.REMOVE)
	public CommonResult<Void> remove(@RequestBody TenantRemoveParam removeParam) {
        service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(APIConst.APIOperation.TenantTitle.GET)
    @ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TenantGetParam", required = true)
	@GetMapping(APIPath.TenantPath.GET)
	public CommonResult<Tenant> get(TenantGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
    }

}
