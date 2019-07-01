package com.github.attemper.web.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.web.service.JobOperatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("Job")
@RestController
public class JobController {
	
	@Autowired
	private JobService jobService;

	@Autowired
	private JobOperatedService jobOperatedService;

	@ApiOperation("List jobs")
	@ApiImplicitParam(value = "JobListParam", name = "param", dataType = "JobListParam", required = true)
	@GetMapping(APIPath.JobPath.$)
	public CommonResult<Map<String, Object>> list(JobListParam param) {
		return CommonResult.putResult(jobOperatedService.list(param));
	}

	@ApiOperation("Add job")
	@ApiImplicitParam(value = "JobSaveParam", name = "param", dataType = "JobSaveParam", required = true)
	@PostMapping(APIPath.JobPath.$)
	public CommonResult<Job> add(@RequestBody JobSaveParam param) {
		return CommonResult.putResult(jobOperatedService.add(param));
	}

	@ApiOperation("Update job")
	@ApiImplicitParam(value = "JobSaveParam", name = "param", dataType = "JobSaveParam", required = true)
	@PutMapping(APIPath.JobPath.$)
	public CommonResult<Job> update(@RequestBody JobSaveParam param) {
		return CommonResult.putResult(jobOperatedService.update(param));
	}

	@ApiOperation("Remove jobs")
	@ApiImplicitParam(value = "JobNamesParam", name = "param", dataType = "JobNamesParam", required = true)
	@DeleteMapping(APIPath.JobPath.$)
	public CommonResult<Void> remove(@RequestBody JobNamesParam param) {
		return CommonResult.putResult(jobOperatedService.remove(param));
	}

	@ApiOperation("Get job")
	@ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
	@GetMapping(APIPath.JobPath.GET)
	public CommonResult<Job> get(JobGetParam param) {
		return CommonResult.putResult(jobService.get(param));
	}

	@ApiOperation("List jobs by versions")
	@ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
	@GetMapping(APIPath.JobPath.VERSIONS)
	public CommonResult<List<Job>> listVersions(JobGetParam param) {
		return CommonResult.putResult(jobService.versions(param));
	}

	@ApiOperation("List args of job")
	@ApiImplicitParam(value = "JobArgListParam", name = "param", dataType = "JobArgListParam", required = true)
	@GetMapping(APIPath.JobPath.ARG)
	public CommonResult<Map<String, Object>> listArg(JobArgListParam param) {
		return CommonResult.putResult(jobService.listArg(param));
	}

	@ApiOperation("Get project")
	@ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
	@GetMapping(APIPath.JobPath.GET_PROJECT)
	public CommonResult<Project> getProject(JobGetParam param) {
		return CommonResult.putResult(jobService.getProject(param));
	}

	@ApiOperation("Save project of job")
	@ApiImplicitParam(value = "JobProjectSaveParam", name = "param", dataType = "JobProjectSaveParam", required = true)
	@PutMapping(APIPath.JobPath.SAVE_PROJECT)
	public CommonResult<Void> saveProject(@RequestBody JobProjectSaveParam param) {
		return CommonResult.putResult(jobService.saveProject(param));
	}

	@ApiOperation("Publish job to quartz and camunda")
	@ApiImplicitParam(value = "JobPublishParam", name = "param", dataType = "JobPublishParam", required = true)
	@PutMapping(APIPath.JobPath.PUBLISH)
	public CommonResult<Void> publish(@RequestBody JobPublishParam param) {
		return CommonResult.putResult(jobOperatedService.publish(param));
	}

	@ApiOperation("Copy job")
	@ApiImplicitParam(value = "JobCopyParam", name = "param", dataType = "JobCopyParam", required = true)
	@PutMapping(APIPath.JobPath.COPY)
	public CommonResult<Job> copy(@RequestBody JobCopyParam param) {
		return CommonResult.putResult(jobOperatedService.copy(param));
	}

	@ApiOperation("Exchange current reversion to the latest reversion")
	@ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
	@PutMapping(APIPath.JobPath.EXCHANGE)
	public CommonResult<Job> exchange(@RequestBody JobGetParam param) {
		return CommonResult.putResult(jobOperatedService.exchange(param));
	}

	@ApiOperation("Manual start jobs")
	@ApiImplicitParam(value = "JobNamesParam", name = "param", dataType = "JobNamesParam", required = true)
	@PostMapping(APIPath.JobPath.MANUAL)
	public CommonResult<Void> manual(@RequestBody JobNamesParam param) {
		return CommonResult.putResult(jobOperatedService.manual(param));
	}

	@ApiOperation("Add arg")
	@ApiImplicitParam(value = "JobArgAllocatedParam", name = "param", dataType = "JobArgAllocatedParam", required = true)
	@PostMapping(APIPath.JobPath.ARG)
	public CommonResult<Void> addArg(@RequestBody JobArgAllocatedParam param) {
		return CommonResult.putResult(jobOperatedService.addArg(param));
	}

	@ApiOperation("Remove arg")
	@ApiImplicitParam(value = "JobArgAllocatedParam", name = "param", dataType = "JobArgAllocatedParam", required = true)
	@DeleteMapping(APIPath.JobPath.ARG)
	public CommonResult<Void> removeArg(@RequestBody JobArgAllocatedParam param) {
		return CommonResult.putResult(jobOperatedService.removeArg(param));
	}
}
