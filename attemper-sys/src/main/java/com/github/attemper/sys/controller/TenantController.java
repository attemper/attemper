package com.github.attemper.sys.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.sys.tenant.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.role.Role;
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

	@RequiresPermissions("tenant-update")
	@ApiOperation("Update password")
	@ApiImplicitParam(value = "TenantChangePasswordParam", name = "param", dataType = "TenantChangePasswordParam", required = true)
	@PutMapping(APIPath.TenantPath.PASSWORD)
	public CommonResult<Void> updatePassword(@RequestBody TenantChangePasswordParam param) {
		return CommonResult.putResult(service.updatePassword(param));
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
    public CommonResult<Void> remove(@RequestBody TenantNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

	@ApiOperation("Get tenant")
	@ApiImplicitParam(value = "TenantGetParam", name = "param", dataType = "TenantGetParam", required = true)
	@GetMapping(APIPath.TenantPath.GET)
	public CommonResult<Tenant> get(TenantNameParam param) {
		Tenant tenant = service.get(param);
		tenant.setPassword(null);
		return CommonResult.putResult(tenant);
    }

	@ApiOperation("Get role")
	@ApiImplicitParam(value = "UserGetParam", name = "param", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.TenantPath.ROLE)
	public CommonResult<List<Role>> getRoles(TenantNameParam getParam) {
		return CommonResult.putResult(service.getRoles(getParam));
	}

	@ApiOperation("Update role")
	@ApiImplicitParam(value = "TenantRoleSaveParam", name = "param", dataType = "TenantRoleSaveParam", required = true)
	@PutMapping(APIPath.TenantPath.ROLE)
	public CommonResult saveRoles(@RequestBody TenantRoleSaveParam param) {
		return CommonResult.putResult(service.saveRoles(param));
	}
}
