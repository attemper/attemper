package com.sse.attemper.scheduler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.job.BaseJobRemoveParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.scheduler.handler.JobDeletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobDeletedController {

    @Autowired
    private JobDeletedService handler;

    @DeleteMapping(APIPath.SchedulerPath.JOB)
    public CommonResult<Void> deleteJob(@RequestBody BaseJobRemoveParam param) throws JsonProcessingException {
        return CommonResult.putResult(handler.deleteJob(param));
    }
}
