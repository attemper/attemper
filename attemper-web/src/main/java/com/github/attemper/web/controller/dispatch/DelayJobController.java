package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.param.dispatch.delay.DelayJobListParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.java.sdk.common.web.constant.WebAPIPath;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobIdsParam;
import com.github.attemper.java.sdk.common.web.param.delay.DelayJobParam;
import com.github.attemper.java.sdk.common.web.result.delay.DelayJobResult;
import com.github.attemper.web.service.dispatch.DelayJobOperatedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name ="Delay Job")
@RestController
public class DelayJobController {

    @Autowired
    private DelayJobOperatedService service;

    @Operation(summary = "List delay jobs", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(WebAPIPath.DelayJobPath.$)
    public CommonResult<Map<String, Object>> list(DelayJobListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @Operation(summary = "Add job", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(WebAPIPath.DelayJobPath.$)
    public CommonResult<DelayJobResult> add(@RequestBody DelayJobParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @Operation(summary = "Delete delay jobs", parameters = {@Parameter(name = "param", required = true)})
    @DeleteMapping(WebAPIPath.DelayJobPath.$)
    public CommonResult<Void> remove(@RequestBody DelayJobIdsParam param) {
        return CommonResult.putResult(service.remove(param));
    }
}
