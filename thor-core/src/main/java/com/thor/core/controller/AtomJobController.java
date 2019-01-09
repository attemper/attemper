package com.thor.core.controller;

import com.stark.sdk.common.result.CommonResult;
import com.thor.core.service.AtomJobService;
import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.job.atom.AtomJobListParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @auth ldang
 */
@Api(tags = ThorAPIConst.Tag.ATOM_JOB)
@RestController
public class AtomJobController {
	
	@Autowired
	private AtomJobService service;

	/*
	@ApiOperation(StarkAPIConst.Operation.Tenant.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PostMapping(StarkAPIPath.Tenant.ADD)
	public CommonResult add(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(StarkAPIConst.Operation.Tenant.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PutMapping(StarkAPIPath.Tenant.UPDATE)
	public CommonResult update(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}*/

	@ApiOperation(ThorAPIConst.Operation.AtomJob.LIST)
    @ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "AtomJobListParam", required = true)
	@GetMapping(ThorAPIPath.Job.AtomJob.LIST)
	public CommonResult list(AtomJobListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}
/*
	@ApiOperation(StarkAPIConst.Operation.Tenant.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "TenantRemoveParam", required = true)
	@DeleteMapping(StarkAPIPath.Tenant.REMOVE)
	public CommonResult remove(@RequestBody TenantRemoveParam removeParam) {
        service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(StarkAPIConst.Operation.Tenant.GET)
    @ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TenantGetParam", required = true)
	@GetMapping(StarkAPIPath.Tenant.GET)
	public CommonResult get(TenantGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
    }*/

}
