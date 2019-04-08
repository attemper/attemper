package com.sse.attemper.core.controller.arg;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.arg.ArgGetParam;
import com.sse.attemper.common.param.dispatch.arg.ArgListParam;
import com.sse.attemper.common.param.dispatch.arg.ArgRemoveParam;
import com.sse.attemper.common.param.dispatch.arg.ArgSaveParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.arg.Arg;
import com.sse.attemper.core.service.arg.ArgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = APIConst.APITag.ARG)
@RestController
public class ArgController {

    @Autowired
    private ArgService service;

    @ApiOperation(APIConst.APIOperation.ArgTitle.ADD)
    @ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "ArgSaveParam", required = true)
    @PostMapping(value = APIPath.ArgPath.ADD)
    public CommonResult<Arg> add(@RequestBody ArgSaveParam saveParam) {
        return CommonResult.putResult(service.add(saveParam));
    }

    @ApiOperation(APIConst.APIOperation.ArgTitle.GET)
    @ApiImplicitParam(value = "查询单个参数信息", name = "getParam", dataType = "ArgGetParam", required = true)
    @GetMapping(value = APIPath.ArgPath.GET)
    public CommonResult<Arg> get(ArgGetParam getParam) {
        return CommonResult.putResult(service.get(getParam));
    }

    @ApiOperation(APIConst.APIOperation.ArgTitle.LIST)
    @ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "ArgListParam", required = true)
    @GetMapping(APIPath.ArgPath.LIST)
    public CommonResult<Map<String, Object>> list(ArgListParam listParam) {
        return CommonResult.putResult(service.list(listParam));
    }

    @ApiOperation(APIConst.APIOperation.ArgTitle.UPDATE)
    @ApiImplicitParam(value = "被更新的参数", name = "saveParam", dataType = "ArgSaveParam", required = true)
    @PutMapping(value = APIPath.ArgPath.UPDATE)
    public CommonResult<Arg> update(@RequestBody ArgSaveParam saveParam) {
        return CommonResult.putResult(service.update(saveParam));
    }

    @ApiOperation(APIConst.APIOperation.ArgTitle.REMOVE)
    @ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "ArgRemoveParam", required = true)
    @DeleteMapping(value = APIPath.ArgPath.REMOVE)
    public CommonResult remove(@RequestBody ArgRemoveParam removeParam) {
        service.remove(removeParam);
        return CommonResult.ok();
    }

}
