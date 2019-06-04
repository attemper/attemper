package com.github.attemper.executor.controller;

import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.log.LogService;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.executor.param.log.LogParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Appending log")
@RestController
public class LogController {

    @Autowired
    private LogService service;

    @PostMapping(ExecutorAPIPath.APPEND_LOG)
    public CommonResult<Void> appendLog(@RequestBody LogParam logParam) {
        return CommonResult.putResult(service.appendLog(logParam));
    }

}
