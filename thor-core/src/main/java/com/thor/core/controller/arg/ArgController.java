package com.thor.core.controller.arg;

import com.thor.core.service.arg.ArgService;
import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.dispatch.arg.ArgGetParam;
import com.thor.sdk.common.param.dispatch.arg.ArgListParam;
import com.thor.sdk.common.param.dispatch.arg.ArgRemoveParam;
import com.thor.sdk.common.param.dispatch.arg.ArgSaveParam;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sdk.common.result.dispatch.arg.Arg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = ThorAPIConst.APITag.ARG)
@RestController
public class ArgController {

    @Autowired
    private ArgService service;

    @ApiOperation(ThorAPIConst.APIOperation.ArgTitle.ADD)
    @ApiImplicitParam(value = "被新增的数据", name = "saveParam", dataType = "ArgSaveParam", required = true)
    @PostMapping(value = ThorAPIPath.ArgPath.ADD)
    public CommonResult<Arg> add(@RequestBody ArgSaveParam saveParam) {
        return CommonResult.putResult(service.add(saveParam));
    }

    @ApiOperation(ThorAPIConst.APIOperation.ArgTitle.GET)
    @ApiImplicitParam(value = "查询单个参数信息", name = "getParam", dataType = "ArgGetParam", required = true)
    @GetMapping(value = ThorAPIPath.ArgPath.GET)
    public CommonResult<Arg> get(ArgGetParam getParam) {
        return CommonResult.putResult(service.get(getParam));
    }

    @ApiOperation(ThorAPIConst.APIOperation.ArgTitle.LIST)
    @ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "ArgListParam", required = true)
    @GetMapping(ThorAPIPath.ArgPath.LIST)
    public CommonResult<Map<String, Object>> list(ArgListParam listParam) {
        return CommonResult.putResult(service.list(listParam));
    }

    @ApiOperation(ThorAPIConst.APIOperation.ArgTitle.UPDATE)
    @ApiImplicitParam(value = "被更新的参数", name = "saveParam", dataType = "ArgSaveParam", required = true)
    @PutMapping(value = ThorAPIPath.ArgPath.UPDATE)
    public CommonResult<Arg> update(@RequestBody ArgSaveParam saveParam) {
        return CommonResult.putResult(service.update(saveParam));
    }

    @ApiOperation(ThorAPIConst.APIOperation.ArgTitle.REMOVE)
    @ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "ArgRemoveParam", required = true)
    @DeleteMapping(value = ThorAPIPath.ArgPath.REMOVE)
    public CommonResult remove(@RequestBody ArgRemoveParam removeParam) {
        service.remove(removeParam);
        return CommonResult.ok();
    }

}
