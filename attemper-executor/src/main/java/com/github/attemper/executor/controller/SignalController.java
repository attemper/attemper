package com.github.attemper.executor.controller;

import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.core.SignalService;
import com.github.attemper.java.sdk.common.executor.constant.ExecutorAPIPath;
import com.github.attemper.java.sdk.common.executor.param.execution.EndParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * async job call this controller to notify the waited execution id
 */
@RestController
public class SignalController {

    @Autowired
    private SignalService service;

    @PostMapping(ExecutorAPIPath.SIGNAL)
    public CommonResult<Void> signal(@RequestBody EndParam endParam) {
        return CommonResult.putResult(service.signal(endParam));
    }
}
