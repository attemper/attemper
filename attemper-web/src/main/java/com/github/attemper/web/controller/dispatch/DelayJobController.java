package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.param.dispatch.delay.DelayJobListParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.java.sdk.common.web.constant.WebAPIPath;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobIdsParam;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobParam;
import com.github.attemper.java.sdk.common.web.result.delay.DelayJobResult;
import com.github.attemper.web.service.dispatch.DelayJobOperatedService;
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
    @ApiImplicitParam(value = "DelayJobParam", name = "param", dataType = "DelayJobParam", required = true)
    @PostMapping(WebAPIPath.DelayJobPath.$)
    public CommonResult<DelayJobResult> add(@RequestBody DelayJobParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation("Delete delay jobs")
    @ApiImplicitParam(value = "DelayJobIdsParam", name = "param", dataType = "DelayJobIdsParam", required = true)
    @DeleteMapping(WebAPIPath.DelayJobPath.$)
    public CommonResult<Void> remove(@RequestBody DelayJobIdsParam param) {
        return CommonResult.putResult(service.remove(param));
    }
}
