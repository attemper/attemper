package com.thor.core.controller;

import com.thor.common.constant.APIPath;
import com.thor.common.result.CommonResult;
import com.thor.config.constant.APIConst;
import com.thor.core.param.tenant.TenantGetParam;
import com.thor.core.param.tenant.TenantListParam;
import com.thor.core.param.tenant.TenantRemoveParam;
import com.thor.core.param.tenant.TenantSaveParam;
import com.thor.core.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/** 
 * @auth ldang
 */
@Api(tags = APIConst.Tag.TENANT)
@RestController
@RequestMapping(APIPath.Sys.TENANT)
public class TenantController {
	
	@Autowired
	private TenantService service;

    @ApiOperation(APIConst.Operation.Tenant.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PostMapping(APIPath.ADD)
	public CommonResult add(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(APIConst.Operation.Tenant.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TenantSaveParam", required = true)
	@PutMapping(APIPath.UPDATE)
	public CommonResult update(@RequestBody TenantSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

    @ApiOperation(APIConst.Operation.Tenant.LIST)
    @ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "TenantListParam", required = true)
	@GetMapping(APIPath.LIST)
	public CommonResult list(TenantListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(APIConst.Operation.Tenant.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "TenantRemoveParam", required = true)
	@DeleteMapping(APIPath.REMOVE)
	public CommonResult remove(@RequestBody TenantRemoveParam removeParam){
        service.remove(removeParam);
        return CommonResult.ok();
	}

    @ApiOperation(APIConst.Operation.Tenant.GET)
    @ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TenantGetParam", required = true)
    @GetMapping(APIPath.GET)
	public CommonResult get(TenantGetParam getParam) {
        return CommonResult.putResult(service.getTenant(getParam));
    }

}
