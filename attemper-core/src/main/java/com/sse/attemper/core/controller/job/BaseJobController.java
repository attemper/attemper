package com.sse.attemper.core.controller.job;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.job.BaseJobGetParam;
import com.sse.attemper.common.param.dispatch.job.BaseJobListParam;
import com.sse.attemper.common.param.dispatch.job.JobProjectSaveParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.job.BaseJob;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.core.service.job.BaseJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Api(tags = APIConst.APITag.JOB)
@RestController
public class BaseJobController {
	
	@Autowired
	private BaseJobService service;

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "BaseJobListParam", required = true)
	@GetMapping(APIPath.JobPath.BasePath.LIST)
	public CommonResult<Map<String, Object>> list(BaseJobListParam listParam) {
		return CommonResult.putResult(service.list(listParam));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "BaseJobGetParam", required = true)
	@GetMapping(APIPath.JobPath.BasePath.GET)
	public CommonResult<BaseJob> get(BaseJobGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.BaseTitle.VERSIONS)
	@ApiImplicitParam(value = "任务名称参数", name = "getParam", dataType = "BaseJobGetParam", required = true)
	@GetMapping(APIPath.JobPath.BasePath.VERSIONS)
	public CommonResult<List<BaseJob>> listVersions(BaseJobGetParam getParam) {
		return CommonResult.putResult(service.versions(getParam));
	}

	@ApiOperation("get project")
	@ApiImplicitParam(value = "param", name = "getParam", dataType = "BaseJobGetParam", required = true)
	@GetMapping(APIPath.JobPath.BasePath.GET_PROJECT)
	public CommonResult<Project> getProject(BaseJobGetParam getParam) {
		return CommonResult.putResult(service.getProject(getParam));
	}

	@ApiOperation("save project of job")
	@ApiImplicitParam(value = "param", name = "param", dataType = "JobProjectSaveParam", required = true)
	@PutMapping(APIPath.JobPath.BasePath.UPDATE_PROJECT)
	public CommonResult<Void> saveProject(@RequestBody JobProjectSaveParam param) {
		return CommonResult.putResult(service.saveProject(param));
	}
}
