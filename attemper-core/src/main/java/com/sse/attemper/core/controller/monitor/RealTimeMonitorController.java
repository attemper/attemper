package com.sse.attemper.core.controller.monitor;

import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.monitor.JobInstanceListParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.core.service.monitor.RealTimeMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api("RealTimeMonitor")
@RestController
public class RealTimeMonitorController {

    @Autowired
    private RealTimeMonitorService service;

    @ApiOperation("List executing job instances")
    @ApiImplicitParam(value = "JobInstanceListParam", name = "param", dataType = "JobInstanceListParam", required = true)
    @GetMapping(APIPath.MonitorPath.RealTimePath.LIST)
    public CommonResult<Map<String, Object>> list(JobInstanceListParam param) {
        return CommonResult.putResult(service.list(param));
    }
}
