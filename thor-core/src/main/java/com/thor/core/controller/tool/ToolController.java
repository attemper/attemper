package com.thor.core.controller.tool;

import com.thor.core.service.tool.ToolService;
import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.result.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ldang
 */
@Api(tags = ThorAPIConst.APITag.TOOL)
@RestController
public class ToolController {

    @Autowired
    private ToolService service;

    @ApiOperation(ThorAPIConst.APIOperation.ToolTitle.GET_TIME_ZONE)
    @GetMapping(ThorAPIPath.ToolPath.GET_TIME_ZONE)
    public CommonResult<String[]> listTimeZone() {
        return CommonResult.putResult(service.listTimeZone());
    }
}
