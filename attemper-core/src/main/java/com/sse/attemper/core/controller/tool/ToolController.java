package com.sse.attemper.core.controller.tool;

import com.sse.attemper.core.service.tool.ToolService;
import com.sse.attemper.sdk.common.constant.APIConst;
import com.sse.attemper.sdk.common.constant.APIPath;
import com.sse.attemper.sdk.common.result.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ldang
 */
@Api(tags = APIConst.APITag.TOOL)
@RestController
public class ToolController {

    @Autowired
    private ToolService service;

    @ApiOperation(APIConst.APIOperation.ToolTitle.GET_TIME_ZONE)
    @GetMapping(APIPath.ToolPath.GET_TIME_ZONE)
    public CommonResult<String[]> listTimeZone() {
        return CommonResult.putResult(service.listTimeZone());
    }
}
