package com.github.attemper.executor.controller;

import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.core.SignalService;
import com.github.attemper.java.sdk.common.biz2executor.constant.Biz2ExecutorAPIPath;
import com.github.attemper.java.sdk.common.biz2executor.param.end.EndExecutionParam;
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

    @PostMapping(Biz2ExecutorAPIPath.SIGNAL)
    public CommonResult<Void> signal(@RequestBody EndExecutionParam endExecutionParam) {
        return CommonResult.putResult(service.signal(endExecutionParam));
    }
}
