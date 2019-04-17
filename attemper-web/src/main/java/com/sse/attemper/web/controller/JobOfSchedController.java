package com.sse.attemper.web.controller;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.job.*;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.job.FlowJob;
import com.sse.attemper.web.service.job.JobOfSchedService;
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
public class JobOfSchedController {

    @Autowired
    private JobOfSchedService service;

    @ApiOperation(APIConst.APIOperation.JobTitle.ADD)
    @ApiImplicitParam(value = "被新增的数据", name = "param", dataType = "JobSaveParam", required = true)
    @PostMapping(APIPath.JobPath.ADD)
    public CommonResult<FlowJob> add(@RequestBody JobSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation(APIConst.APIOperation.JobTitle.UPDATE)
    @ApiImplicitParam(value = "被更新的数据", name = "param", dataType = "JobSaveParam", required = true)
    @PutMapping(APIPath.JobPath.UPDATE)
    public CommonResult<FlowJob> update(@RequestBody JobSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

    @ApiOperation(APIConst.APIOperation.JobTitle.REMOVE)
    @ApiImplicitParam(value = "被删除的主键数组", name = "param", dataType = "JobNamesParam", required = true)
    @DeleteMapping(APIPath.JobPath.REMOVE)
    public CommonResult<Void> remove(@RequestBody JobNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @ApiOperation(APIConst.APIOperation.JobTitle.PUBLISH)
    @ApiImplicitParam(value = "任务名称列表参数", name = "param", dataType = "JobPublishParam", required = true)
    @PutMapping(APIPath.JobPath.PUBLISH)
    public CommonResult<Void> publish(@RequestBody JobPublishParam param) {
        return CommonResult.putResult(service.publish(param));
    }

    @ApiOperation(APIConst.APIOperation.JobTitle.COPY)
    @ApiImplicitParam(value = "任务名称参数", name = "param", dataType = "JobGetParam", required = true)
    @PutMapping(APIPath.JobPath.COPY)
    public CommonResult<FlowJob> copy(@RequestBody JobCopyParam param) {
        return CommonResult.putResult(service.copy(param));
    }

    @ApiOperation(APIConst.APIOperation.JobTitle.EXCHANGE)
    @ApiImplicitParam(value = "job name", name = "param", dataType = "JobGetParam", required = true)
    @PutMapping(APIPath.JobPath.EXCHANGE)
    public CommonResult<FlowJob> exchange(@RequestBody JobGetParam param) {
        return CommonResult.putResult(service.exchange(param));
    }

    @ApiOperation(APIConst.APIOperation.JobTitle.MANUAL)
    @ApiImplicitParam(value = "job names", name = "param", dataType = "JobNamesParam", required = true)
    @PostMapping(APIPath.JobPath.MANUAL)
    public CommonResult<Void> manual(@RequestBody JobNamesParam param) {
        return CommonResult.putResult(service.manual(param));
    }
}
