package com.github.attemper.executor.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.executor.JobInvokingResult;
import com.github.attemper.executor.disruptor.producer.RequestProducer;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Invoke job")
@RestController
public class JobInvokingController {

    @Autowired
    private RequestProducer requestProducer;

    @PostMapping(APIPath.ExecutorPath.JOB_INVOKING)
    public CommonResult<JobInvokingResult> invokeJob(@RequestBody JobInvokingParam jobInvokingParam) {
        CommonResult<JobInvokingResult> commonResult = null;
        requestProducer.onData(jobInvokingParam, commonResult);
        return commonResult;
    }
}
