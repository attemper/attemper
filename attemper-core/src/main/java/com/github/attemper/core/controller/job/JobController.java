package com.github.attemper.core.controller.job;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.job.JobArgListParam;
import com.github.attemper.common.param.dispatch.job.JobGetParam;
import com.github.attemper.common.param.dispatch.job.JobProjectSaveParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.core.service.job.JobService;
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
@Api("Job")
@RestController
public class JobController {
	
	@Autowired
	private JobService service;

	@ApiOperation("Get job")
	@ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
	@GetMapping(APIPath.JobPath.GET)
	public CommonResult<Job> get(JobGetParam param) {
		return CommonResult.putResult(service.get(param));
	}

	@ApiOperation("List jobs by versions")
	@ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
	@GetMapping(APIPath.JobPath.VERSIONS)
	public CommonResult<List<Job>> listVersions(JobGetParam param) {
		return CommonResult.putResult(service.versions(param));
	}

	@ApiOperation("List args of job")
	@ApiImplicitParam(value = "JobArgListParam", name = "param", dataType = "JobArgListParam", required = true)
	@GetMapping(APIPath.JobPath.LIST_ARG)
	public CommonResult<Map<String, Object>> listArg(JobArgListParam param) {
		return CommonResult.putResult(service.listArg(param));
	}

	@ApiOperation("Get project")
	@ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
	@GetMapping(APIPath.JobPath.GET_PROJECT)
	public CommonResult<Project> getProject(JobGetParam param) {
		return CommonResult.putResult(service.getProject(param));
	}

	@ApiOperation("Save project of job")
	@ApiImplicitParam(value = "JobProjectSaveParam", name = "param", dataType = "JobProjectSaveParam", required = true)
	@PutMapping(APIPath.JobPath.UPDATE_PROJECT)
	public CommonResult<Void> saveProject(@RequestBody JobProjectSaveParam param) {
		return CommonResult.putResult(service.saveProject(param));
	}
}
