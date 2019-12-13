package com.github.attemper.core.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.tool.ServerInfoResult;
import com.github.attemper.core.service.tool.ToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Server info")
@RestController
public class ServerInfoController {

    @Autowired
    private ToolService toolService;

    @ApiOperation("Response server info")
    @PostMapping(APIPath.CommonPath.SERVER_INFO)
    public CommonResult<ServerInfoResult> responseServerInfo() {
        return CommonResult.putResult(toolService.responseServerInfo());
    }
}
