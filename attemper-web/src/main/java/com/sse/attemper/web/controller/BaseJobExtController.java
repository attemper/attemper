package com.sse.attemper.web.controller;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.job.*;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.job.BaseJob;
import com.sse.attemper.web.service.job.BaseJobExtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ldang
 */
@Api(tags = APIConst.APITag.JOB)
@RestController
public class BaseJobExtController {
	
	@Autowired
	private BaseJobExtService service;

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.ADD)
	@ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "BaseJobSaveParam", required = true)
	@PostMapping(APIPath.JobPath.BasePath.ADD)
	public CommonResult<BaseJob> add(@RequestBody BaseJobSaveParam saveParam) {
		return CommonResult.putResult(service.add(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "BaseJobSaveParam", required = true)
	@PutMapping(APIPath.JobPath.BasePath.UPDATE)
	public CommonResult<BaseJob> update(@RequestBody BaseJobSaveParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "BaseJobRemoveParam", required = true)
	@DeleteMapping(APIPath.JobPath.BasePath.REMOVE)
	public CommonResult<Void> remove(@RequestBody BaseJobRemoveParam removeParam) {
		return CommonResult.putResult(service.remove(removeParam));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.PUBLISH)
	@ApiImplicitParam(value = "任务名称列表参数", name = "param", dataType = "BaseJobPublishParam", required = true)
	@PutMapping(APIPath.JobPath.BasePath.PUBLISH)
	public CommonResult<Void> publish(@RequestBody BaseJobPublishParam param) {
		return CommonResult.putResult(service.publish(param));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.COPY)
	@ApiImplicitParam(value = "任务名称参数", name = "param", dataType = "BaseJobGetParam", required = true)
	@PutMapping(APIPath.JobPath.BasePath.COPY)
	public CommonResult<BaseJob> copy(@RequestBody BaseJobCopyParam param) {
		return CommonResult.putResult(service.copy(param));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.EXCHANGE)
	@ApiImplicitParam(value = "任务名称参数", name = "param", dataType = "BaseJobGetParam", required = true)
	@PutMapping(APIPath.JobPath.BasePath.EXCHANGE)
	public CommonResult<BaseJob> exchange(@RequestBody BaseJobGetParam param) {
		return CommonResult.putResult(service.exchange(param));
	}
}
