package com.github.attemper.interior.controller;

import com.github.attemper.interior.service.RouterService;
import com.github.attemper.java.sdk.common.executor2biz.constant.Executor2BizAPIPath;
import com.github.attemper.java.sdk.common.executor2biz.param.RouterParam;
import com.github.attemper.java.sdk.common.executor2biz.result.LogResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Async Router")
@RestController
public class AsyncRouterController {

    @Autowired
    private RouterService service;

    @ApiOperation("route the async http task invoke")
    @ApiImplicitParam(value = "RouterParam", name = "param", dataType = "RouterParam", required = true)
    @PostMapping(value = Executor2BizAPIPath.ROUTER_PATH_ASYNC)
    public LogResult asyncRouter(@RequestBody RouterParam routerParam) {
        return (LogResult) service.route(routerParam);
    }
}
