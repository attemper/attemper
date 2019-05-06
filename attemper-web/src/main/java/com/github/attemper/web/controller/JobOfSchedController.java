package com.github.attemper.web.controller;

import com.github.attemper.web.service.job.JobOfSchedService;
import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.job.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.job.FlowJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ldang
 */
@Api("Job")
@RestController
public class JobOfSchedController {

    @Autowired
    private JobOfSchedService service;

    @ApiOperation("Add job")
    @ApiImplicitParam(value = "JobSaveParam", name = "param", dataType = "JobSaveParam", required = true)
    @PostMapping(APIPath.JobPath.ADD)
    public CommonResult<FlowJob> add(@RequestBody JobSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation("Update job")
    @ApiImplicitParam(value = "JobSaveParam", name = "param", dataType = "JobSaveParam", required = true)
    @PutMapping(APIPath.JobPath.UPDATE)
    public CommonResult<FlowJob> update(@RequestBody JobSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

    @ApiOperation("Remove jobs")
    @ApiImplicitParam(value = "JobNamesParam", name = "param", dataType = "JobNamesParam", required = true)
    @DeleteMapping(APIPath.JobPath.REMOVE)
    public CommonResult<Void> remove(@RequestBody JobNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @ApiOperation("Publish job to quartz and camunda")
    @ApiImplicitParam(value = "JobPublishParam", name = "param", dataType = "JobPublishParam", required = true)
    @PutMapping(APIPath.JobPath.PUBLISH)
    public CommonResult<Void> publish(@RequestBody JobPublishParam param) {
        return CommonResult.putResult(service.publish(param));
    }

    @ApiOperation("Copy job")
    @ApiImplicitParam(value = "JobCopyParam", name = "param", dataType = "JobCopyParam", required = true)
    @PutMapping(APIPath.JobPath.COPY)
    public CommonResult<FlowJob> copy(@RequestBody JobCopyParam param) {
        return CommonResult.putResult(service.copy(param));
    }

    @ApiOperation("Exchange current reversion to the latest reversion")
    @ApiImplicitParam(value = "JobGetParam", name = "param", dataType = "JobGetParam", required = true)
    @PutMapping(APIPath.JobPath.EXCHANGE)
    public CommonResult<FlowJob> exchange(@RequestBody JobGetParam param) {
        return CommonResult.putResult(service.exchange(param));
    }

    @ApiOperation("Manual start jobs")
    @ApiImplicitParam(value = "JobNamesParam", name = "param", dataType = "JobNamesParam", required = true)
    @PostMapping(APIPath.JobPath.MANUAL)
    public CommonResult<Void> manual(@RequestBody JobNamesParam param) {
        return CommonResult.putResult(service.manual(param));
    }

    @ApiOperation("Add arg")
    @ApiImplicitParam(value = "JobArgAllocatedParam", name = "param", dataType = "JobArgAllocatedParam", required = true)
    @PostMapping(APIPath.JobPath.ADD_ARG)
    public CommonResult<Void> addArg(@RequestBody JobArgAllocatedParam param) {
        return CommonResult.putResult(service.addArg(param));
    }

    @ApiOperation("Remove arg")
    @ApiImplicitParam(value = "JobArgAllocatedParam", name = "param", dataType = "JobArgAllocatedParam", required = true)
    @DeleteMapping(APIPath.JobPath.REMOVE_ARG)
    public CommonResult<Void> removeArg(@RequestBody JobArgAllocatedParam param) {
        return CommonResult.putResult(service.removeArg(param));
    }
}
