package com.thor.core.controller.job;

import com.thor.core.service.job.BaseJobService;
import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.dispatch.job.BaseJobGetParam;
import com.thor.sdk.common.param.dispatch.job.BaseJobListParam;
import com.thor.sdk.common.param.dispatch.job.BaseJobRemoveParam;
import com.thor.sdk.common.param.dispatch.job.BaseJobSaveParam;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sdk.common.result.dispatch.job.BaseJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ldang
 */
@Api(tags = ThorAPIConst.APITag.JOB)
@RestController
public class BaseJobController {
	
	@Autowired
	private BaseJobService service;

	@ApiOperation(ThorAPIConst.APIOperation.JobTitle.BaseTitle.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "BaseJobSaveParam", required = true)
	@PostMapping(ThorAPIPath.JobPath.BasePath.ADD)
	public CommonResult<BaseJob> add(@RequestBody BaseJobSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.JobTitle.BaseTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "BaseJobSaveParam", required = true)
	@PutMapping(ThorAPIPath.JobPath.BasePath.UPDATE)
	public CommonResult<BaseJob> update(@RequestBody BaseJobSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.JobTitle.BaseTitle.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "BaseJobListParam", required = true)
	@GetMapping(ThorAPIPath.JobPath.BasePath.LIST)
	public CommonResult<Map<String, Object>> list(BaseJobListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.JobTitle.BaseTitle.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "BaseJobRemoveParam", required = true)
	@DeleteMapping(ThorAPIPath.JobPath.BasePath.REMOVE)
	public CommonResult remove(@RequestBody BaseJobRemoveParam removeParam) {
        service.remove(removeParam);
		return CommonResult.ok();
	}

	@ApiOperation(ThorAPIConst.APIOperation.JobTitle.BaseTitle.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "BaseJobGetParam", required = true)
	@GetMapping(ThorAPIPath.JobPath.BasePath.GET)
	public CommonResult<BaseJob> get(BaseJobGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

}
