package com.github.attemper.executor.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.disruptor.producer.RequestProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name ="Invoke job")
@RestController
public class JobInvokingController {

    @Autowired
    private RequestProducer requestProducer;

    @Operation(summary = "Invoke a job", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(APIPath.ExecutorPath.JOB_INVOKING)
    public CommonResult<Void> invokeJob(@RequestBody JobInvokingParam param) {
        requestProducer.onData(param);
        return CommonResult.ok();
    }
}
