package com.github.attemper.executor.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.ProgramUpdatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Update program package")
@RestController
public class ProgramUpdatedController {

    @Autowired
    private ProgramUpdatedService service;

    @ApiOperation("Load package")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @PostMapping(APIPath.ExecutorPath.LOAD_PACKAGE)
    public CommonResult<Void> loadPackage(@RequestBody IdParam param) {
        return CommonResult.putResult(service.loadPackage(param));
    }

    @ApiOperation("Unload package")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @PostMapping(APIPath.ExecutorPath.UNLOAD_PACKAGE)
    public CommonResult<Void> unloadPackage(@RequestBody IdParam param) {
        return CommonResult.putResult(service.unloadPackage(param));
    }
}
