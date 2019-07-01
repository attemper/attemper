package com.github.attemper.sys.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.sys.tenant.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.sys.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Api("Tenant")
@RestController
public class TenantController {
	
	@Autowired
	private TenantService service;

    @RequiresPermissions("tenant-add")
    @ApiOperation("Add tenant")
    @ApiImplicitParam(value = "TenantSaveParam", name = "param", dataType = "TenantSaveParam", required = true)
	@PostMapping(APIPath.TenantPath.$)
    public CommonResult<Tenant> add(@RequestBody TenantSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @RequiresPermissions("tenant-update")
    @ApiOperation("Update tenant")
    @ApiImplicitParam(value = "TenantSaveParam", name = "param", dataType = "TenantSaveParam", required = true)
	@PutMapping(APIPath.TenantPath.$)
    public CommonResult<Tenant> update(@RequestBody TenantSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

	@ApiOperation("List tenants")
	@ApiImplicitParam(value = "TenantListParam", name = "param", dataType = "TenantListParam", required = true)
	@GetMapping(APIPath.TenantPath.$)
	public CommonResult<Map<String, Object>> list(TenantListParam param) {
		return CommonResult.putResult(service.list(param));
	}

    @RequiresPermissions("tenant-remove")
    @ApiOperation("Remove tenants")
    @ApiImplicitParam(value = "TenantRemoveParam", name = "param", dataType = "TenantRemoveParam", required = true)
	@DeleteMapping(APIPath.TenantPath.$)
    public CommonResult<Void> remove(@RequestBody TenantRemoveParam param) {
        return CommonResult.putResult(service.remove(param));
    }

	@ApiOperation("Get tenant")
	@ApiImplicitParam(value = "TenantGetParam", name = "param", dataType = "TenantGetParam", required = true)
	@GetMapping(APIPath.TenantPath.GET)
	public CommonResult<Tenant> get(TenantGetParam param) {
		Tenant tenant = service.get(param);
		tenant.setPassword(null);
		return CommonResult.putResult(tenant);
    }

	@ApiOperation("Get tag")
	@ApiImplicitParam(value = "UserGetParam", name = "param", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.TenantPath.TAG)
	public CommonResult<List<Tag>> getTags(TenantGetParam getParam) {
		return CommonResult.putResult(service.getTags(getParam));
	}

	@ApiOperation("Update tag")
	@ApiImplicitParam(value = "TenantTagSaveParam", name = "param", dataType = "TenantTagSaveParam", required = true)
	@PutMapping(APIPath.TenantPath.TAG)
	public CommonResult saveTags(@RequestBody TenantTagSaveParam param) {
		service.saveTags(param);
		return CommonResult.ok();
	}
}
