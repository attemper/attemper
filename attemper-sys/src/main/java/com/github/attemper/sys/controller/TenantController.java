package com.github.attemper.sys.controller;

import com.github.attemper.sys.service.TenantService;
import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.param.sys.tenant.TenantListParam;
import com.github.attemper.common.param.sys.tenant.TenantRemoveParam;
import com.github.attemper.common.param.sys.tenant.TenantSaveParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.tenant.Tenant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ldang
 */
@Api("Tenant")
@RestController
public class TenantController {
	
	@Autowired
	private TenantService service;

	@ApiOperation("Add tenant")
	@ApiImplicitParam(value = "TenantSaveParam", name = "param", dataType = "TenantSaveParam", required = true)
	@PostMapping(APIPath.TenantPath.ADD)
	public CommonResult<Tenant> add(@RequestBody TenantSaveParam param) {
		return CommonResult.putResult(service.add(param));
	}

	@ApiOperation("Update tenant")
	@ApiImplicitParam(value = "TenantSaveParam", name = "param", dataType = "TenantSaveParam", required = true)
	@PutMapping(APIPath.TenantPath.UPDATE)
	public CommonResult<Tenant> update(@RequestBody TenantSaveParam param) {
		return CommonResult.putResult(service.update(param));
	}

	@ApiOperation("List tenants")
	@ApiImplicitParam(value = "TenantListParam", name = "param", dataType = "TenantListParam", required = true)
	@GetMapping(APIPath.TenantPath.LIST)
	public CommonResult<Map<String, Object>> list(TenantListParam param) {
		return CommonResult.putResult(service.list(param));
	}

	@ApiOperation("Remove tenants")
	@ApiImplicitParam(value = "TenantRemoveParam", name = "param", dataType = "TenantRemoveParam", required = true)
	@DeleteMapping(APIPath.TenantPath.REMOVE)
	public CommonResult<Void> remove(@RequestBody TenantRemoveParam param) {
		return CommonResult.putResult(service.remove(param));
	}

	@ApiOperation("Get tenant")
	@ApiImplicitParam(value = "TenantGetParam", name = "param", dataType = "TenantGetParam", required = true)
	@GetMapping(APIPath.TenantPath.GET)
	public CommonResult<Tenant> get(TenantGetParam param) {
		return CommonResult.putResult(service.get(param));
    }

}
