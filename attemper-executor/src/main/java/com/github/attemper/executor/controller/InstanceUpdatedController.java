package com.github.attemper.executor.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.InstanceUpdatedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name ="Operate job instance")
@RestController
public class InstanceUpdatedController {

    @Autowired
    private InstanceUpdatedService service;

    @Operation(summary = "Terminate", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(APIPath.ExecutorPath.TERMINATE)
    public CommonResult<Void> terminate(@RequestBody IdParam param) {
        return CommonResult.putResult(service.terminate(param));
    }

}
