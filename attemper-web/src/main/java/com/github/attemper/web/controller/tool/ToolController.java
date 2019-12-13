package com.github.attemper.web.controller.tool;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.tool.server.ServerInfoGetParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.core.service.tool.ToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api("Tool")
@RestController
public class ToolController {

    @Autowired
    private ToolService service;

    @ApiOperation("Get time zones of all")
    @GetMapping(APIPath.ToolPath.GET_TIME_ZONE)
    public CommonResult<String[]> listTimeZone() {
        return CommonResult.putResult(service.listTimeZone());
    }

    @ApiOperation("List services of scheduler")
    @GetMapping(APIPath.ToolPath.LIST_SCHEDULER_SERVICE)
    public CommonResult<List<ServiceInstance>> listSchedulerService() {
        return CommonResult.putResult(service.listSchedulerService());
    }

    @ApiOperation("List services of executor")
    @GetMapping(APIPath.ToolPath.LIST_EXECUTOR_SERVICE)
    public CommonResult<List<ServiceInstance>> listExecutorService() {
        return CommonResult.putResult(service.listExecutorService());
    }

    @ApiOperation("Get server info")
    @GetMapping(APIPath.ToolPath.GET_SERVER_INFO)
    public CommonResult<Map<String, Object>> getServerInfo(ServerInfoGetParam param) {
        return CommonResult.putResult(service.requestServerInfo(param));
    }

    @ApiOperation("Ping the Internet address")
    @GetMapping(APIPath.ToolPath.PING)
    public CommonResult<Boolean> ping(String uri, Integer type) {
        return CommonResult.putResult(service.ping(uri, type));
    }

    @ApiOperation("Get server time")
    @GetMapping(APIPath.ToolPath.TIME)
    public CommonResult<Date> getCurrentTime() {
        return CommonResult.putResult(service.getCurrentTime());
    }

    @ApiOperation("Get arg types")
    @GetMapping(APIPath.ToolPath.ARG_TYPES)
    public CommonResult<List<Map<String, Object>>> listArgType() {
        return CommonResult.putResult(service.listArgType());
    }

    @ApiOperation("Get trade date units")
    @GetMapping(APIPath.ToolPath.TRADE_DATE_UNITS)
    public CommonResult<List<String>> listTradeDateUnit() {
        return CommonResult.putResult(service.listTradeDateUnit());
    }
}
