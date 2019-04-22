package com.sse.attemper.core.controller.monitor;

import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.monitor.JobInstListParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.core.service.monitor.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api("Monitor")
@RestController
public class MonitorController {

    @Autowired
    private MonitorService service;

    @ApiOperation("List job instances")
    @ApiImplicitParam(value = "JobInstListParam", name = "param", dataType = "JobInstListParam", required = true)
    @GetMapping(APIPath.MonitorPath.LIST)
    public CommonResult<Map<String, Object>> list(JobInstListParam param) {
        return CommonResult.putResult(service.list(param));
    }
}
