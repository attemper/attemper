package com.github.attemper.core.controller.monitor;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.monitor.JobInstanceActParam;
import com.github.attemper.common.param.dispatch.monitor.JobInstanceListParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.monitor.JobInstanceAct;
import com.github.attemper.core.service.monitor.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("Monitor")
@RestController
public class MonitorController {

    @Autowired
    private MonitorService service;

    @ApiOperation("List job instances")
    @ApiImplicitParam(value = "JobInstanceListParam", name = "param", dataType = "JobInstanceListParam", required = true)
    @GetMapping(APIPath.MonitorPath.LIST)
    public CommonResult<Map<String, Object>> list(JobInstanceListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("List all activity nodes of one instance")
    @ApiImplicitParam(value = "JobInstanceActParam", name = "param", dataType = "JobInstanceActParam", required = true)
    @GetMapping(APIPath.MonitorPath.LIST_ACT)
    public CommonResult<List<JobInstanceAct>> listAct(JobInstanceActParam param) {
        return CommonResult.putResult(service.listAct(param));
    }
}
