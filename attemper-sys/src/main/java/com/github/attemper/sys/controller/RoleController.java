package com.github.attemper.sys.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.sys.role.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.role.Role;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.sys.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Role")
@RestController
public class RoleController {
	
	@Autowired
	private RoleService service;

	@Operation(summary = "List roles", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.RolePath.$)
	public CommonResult<Map<String, Object>> list(RoleListParam param) {
		return CommonResult.putResult(service.list(param));
	}

	@Operation(summary = "Get role", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.RolePath.GET)
	public CommonResult<Role> get(RoleNameParam param) {
		return CommonResult.putResult(service.get(param));
	}

	@Operation(summary = "Add role", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.RolePath.$)
	public CommonResult<Role> add(@RequestBody RoleSaveParam param) {
		return CommonResult.putResult(service.add(param));
	}

	@Operation(summary = "Update role", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.RolePath.$)
	public CommonResult<Role> update(@RequestBody RoleSaveParam param) {
		return CommonResult.putResult(service.update(param));
	}

	@Operation(summary = "Remove role", parameters = {@Parameter(name = "param", required = true)})
	@DeleteMapping(APIPath.RolePath.$)
	public CommonResult<Void> remove(@RequestBody RoleNamesParam param) {
		return CommonResult.putResult(service.remove(param));
	}

	@Operation(summary = "Get users of role", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.RolePath.TENANT)
	public CommonResult<List<Tenant>> getTenants(RoleNameParam param) {
		return CommonResult.putResult(service.getTenants(param));
	}

	@Operation(summary = "Update users of role", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.RolePath.TENANT)
	public CommonResult<Void> saveTenants(@RequestBody RoleTenantSaveParam param) {
		return CommonResult.putResult(service.saveTenants(param));
    }

	@Operation(summary = "Get resources of role", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.RolePath.RESOURCE)
	public CommonResult<List<String>> getResources(RoleNameParam param) {
		return CommonResult.putResult(service.getResources(param));
	}

	@Operation(summary = "Update resources of role", parameters = {@Parameter(name = "param", required = true)})
	@PutMapping(APIPath.RolePath.RESOURCE)
	public CommonResult<Void> saveResources(@RequestBody RoleResourceSaveParam param) {
		return CommonResult.putResult(service.saveResources(param));
	}
}
