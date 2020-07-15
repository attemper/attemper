package com.github.attemper.sys.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.sys.tenant.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.role.Role;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.sys.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Tenant")
@RestController
public class TenantController {
	
	@Autowired
	private TenantService service;

    @RequiresPermissions("tenant-add")
    @Operation(summary = "Add tenant", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.TenantPath.$)
    public CommonResult<Tenant> add(@RequestBody TenantSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @RequiresPermissions("tenant-update")
    @Operation(summary = "Update tenant", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.TenantPath.$)
    public CommonResult<Tenant> update(@RequestBody TenantSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

	@RequiresPermissions("tenant-update")
	@Operation(summary = "Update password", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.TenantPath.PASSWORD)
	public CommonResult<Void> updatePassword(@RequestBody TenantChangePasswordParam param) {
		return CommonResult.putResult(service.updatePassword(param));
	}

	@Operation(summary = "List tenants", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.TenantPath.$)
	public CommonResult<Map<String, Object>> list(TenantListParam param) {
		return CommonResult.putResult(service.list(param));
	}

    @RequiresPermissions("tenant-remove")
    @Operation(summary = "Remove tenants", parameters = {@Parameter(name = "param", required = true)})
	@DeleteMapping(APIPath.TenantPath.$)
    public CommonResult<Void> remove(@RequestBody TenantNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

	@Operation(summary = "Get tenant", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.TenantPath.GET)
	public CommonResult<Tenant> get(TenantNameParam param) {
		Tenant tenant = service.get(param);
		tenant.setPassword(null);
		return CommonResult.putResult(tenant);
    }

	@Operation(summary = "Get role", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.TenantPath.ROLE)
	public CommonResult<List<Role>> getRoles(TenantNameParam getParam) {
		return CommonResult.putResult(service.getRoles(getParam));
	}

	@Operation(summary = "Update role", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.TenantPath.ROLE)
	public CommonResult saveRoles(@RequestBody TenantRoleSaveParam param) {
		return CommonResult.putResult(service.saveRoles(param));
	}
}
