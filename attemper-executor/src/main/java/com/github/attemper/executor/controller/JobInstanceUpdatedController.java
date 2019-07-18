package com.github.attemper.executor.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.instance.JobInstanceIdParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.core.JobInstanceUpdatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Operate job instance")
@RestController
public class JobInstanceUpdatedController {

    @Autowired
    private JobInstanceUpdatedService service;

    @ApiOperation("Terminate")
    @ApiImplicitParam(value = "JobInstanceIdParam", name = "param", dataType = "JobInstanceIdParam", required = true)
    @PostMapping(APIPath.ExecutorPath.TERMINATE)
    public CommonResult<Void> terminate(@RequestBody JobInstanceIdParam param) {
        return CommonResult.putResult(service.terminate(param));
    }

}
