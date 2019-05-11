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

	@ApiOperation("Get tag")
	@ApiImplicitParam(value = "UserGetParam", name = "param", dataType = "UserGetParam", required = true)
	@GetMapping(APIPath.TenantPath.TAG_LIST)
	public CommonResult<List<Tag>> getTags(TenantGetParam getParam) {
		return CommonResult.putResult(service.getTags(getParam));
	}

	@ApiOperation("Update tag")
	@ApiImplicitParam(value = "TenantTagUpdateParam", name = "param", dataType = "TenantTagUpdateParam", required = true)
	@PutMapping(APIPath.TenantPath.TAG_UPDATE)
	public CommonResult updateTags(@RequestBody TenantTagUpdateParam param) {
		service.updateTags(param);
		return CommonResult.ok();
	}
}
