package com.github.attemper.executor.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.InstanceUpdatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Operate job instance")
@RestController
public class InstanceUpdatedController {

    @Autowired
    private InstanceUpdatedService service;

    @ApiOperation("Terminate")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @PostMapping(APIPath.ExecutorPath.TERMINATE)
    public CommonResult<Void> terminate(@RequestBody IdParam param) {
        return CommonResult.putResult(service.terminate(param));
    }

}
