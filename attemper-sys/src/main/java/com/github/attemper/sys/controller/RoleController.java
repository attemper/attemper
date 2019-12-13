package com.github.attemper.sys.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.sys.role.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.role.Role;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.sys.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("Role")
@RestController
public class RoleController {
	
	@Autowired
	private RoleService service;

	@ApiOperation("List roles")
	@ApiImplicitParam(value = "RoleListParam", name = "param", dataType = "RoleListParam", required = true)
	@GetMapping(APIPath.RolePath.$)
	public CommonResult<Map<String, Object>> list(RoleListParam param) {
		return CommonResult.putResult(service.list(param));
	}

	@ApiOperation("Get role")
	@ApiImplicitParam(value = "RoleGetParam", name = "param", dataType = "RoleGetParam", required = true)
	@GetMapping(APIPath.RolePath.GET)
	public CommonResult<Role> get(RoleNameParam param) {
		return CommonResult.putResult(service.get(param));
	}

	@ApiOperation("Add role")
	@ApiImplicitParam(value = "RoleSaveParam", name = "param", dataType = "RoleSaveParam", required = true)
	@PostMapping(APIPath.RolePath.$)
	public CommonResult<Role> add(@RequestBody RoleSaveParam param) {
		return CommonResult.putResult(service.add(param));
	}

	@ApiOperation("Update role")
	@ApiImplicitParam(value = "RoleSaveParam", name = "param", dataType = "RoleSaveParam", required = true)
	@PutMapping(APIPath.RolePath.$)
	public CommonResult<Role> update(@RequestBody RoleSaveParam param) {
		return CommonResult.putResult(service.update(param));
	}

	@ApiOperation("Remove role")
	@ApiImplicitParam(value = "RoleRemoveParam", name = "param", dataType = "RoleRemoveParam", required = true)
	@DeleteMapping(APIPath.RolePath.$)
	public CommonResult<Void> remove(@RequestBody RoleNamesParam param) {
		return CommonResult.putResult(service.remove(param));
	}

	@ApiOperation("Get users of role")
	@ApiImplicitParam(value = "RoleGetParam", name = "param", dataType = "RoleGetParam", required = true)
	@GetMapping(APIPath.RolePath.TENANT)
	public CommonResult<List<Tenant>> getTenants(RoleNameParam param) {
		return CommonResult.putResult(service.getTenants(param));
	}

	@ApiOperation("Update users of role")
	@ApiImplicitParam(value = "RoleTenantSaveParam", name = "param", dataType = "RoleTenantSaveParam", required = true)
	@PutMapping(APIPath.RolePath.TENANT)
	public CommonResult<Void> saveTenants(@RequestBody RoleTenantSaveParam param) {
		return CommonResult.putResult(service.saveTenants(param));
    }

	@ApiOperation("Get resources of role")
	@ApiImplicitParam(value = "RoleGetParam", name = "param", dataType = "RoleGetParam", required = true)
	@GetMapping(APIPath.RolePath.RESOURCE)
	public CommonResult<List<String>> getResources(RoleNameParam param) {
		return CommonResult.putResult(service.getResources(param));
	}

	@ApiOperation("Update resources of role")
	@ApiImplicitParam(value = "RoleResourceSaveParam", name = "param", dataType = "RoleResourceSaveParam", required = true)
	@PutMapping(APIPath.RolePath.RESOURCE)
	public CommonResult<Void> saveResources(@RequestBody RoleResourceSaveParam param) {
		return CommonResult.putResult(service.saveResources(param));
	}
}
