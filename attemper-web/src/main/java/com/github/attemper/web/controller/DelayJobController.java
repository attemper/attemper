package com.github.attemper.web.controller;

import com.github.attemper.common.param.dispatch.delay.DelayJobListParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.java.sdk.common.web.constant.WebAPIPath;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobExtSaveParam;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobIdsParam;
import com.github.attemper.java.sdk.common.web.result.delay.DelayJobResult;
import com.github.attemper.web.service.DelayJobOperatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api("Delay Job")
@RestController
public class DelayJobController {

    @Autowired
    private DelayJobOperatedService service;

    @ApiOperation("List delay jobs")
    @ApiImplicitParam(value = "DelayJobListParam", name = "param", dataType = "DelayJobListParam", required = true)
    @GetMapping(WebAPIPath.DelayJobPath.$)
    public CommonResult<Map<String, Object>> list(DelayJobListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("Add job")
    @ApiImplicitParam(value = "DelayJobExtSaveParam", name = "param", dataType = "DelayJobExtSaveParam", required = true)
    @PostMapping(WebAPIPath.DelayJobPath.EXT)
    public CommonResult<DelayJobResult> add(@RequestBody DelayJobExtSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation("Delete delay jobs")
    @ApiImplicitParam(value = "JobNamesParam", name = "param", dataType = "JobNamesParam", required = true)
    @DeleteMapping(WebAPIPath.DelayJobPath.$)
    public CommonResult<Void> remove(@RequestBody DelayJobIdsParam param) {
        return CommonResult.putResult(service.remove(param));
    }
}
