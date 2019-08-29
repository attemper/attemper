package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.arg.ArgGetParam;
import com.github.attemper.common.param.dispatch.arg.ArgListParam;
import com.github.attemper.common.param.dispatch.arg.ArgRemoveParam;
import com.github.attemper.common.param.dispatch.arg.ArgSaveParam;
import com.github.attemper.common.param.dispatch.arg.ext.SqlArgParam;
import com.github.attemper.common.param.dispatch.arg.ext.TradeDateArgParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.core.service.dispatch.ArgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("Arg")
@RestController
public class ArgController {

    @Autowired
    private ArgService service;

    @ApiOperation("Add arg")
    @ApiImplicitParam(value = "ArgSaveParam", name = "param", dataType = "ArgSaveParam", required = true)
    @PostMapping(value = APIPath.ArgPath.$)
    public CommonResult<Arg> add(@RequestBody ArgSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation("Update arg")
    @ApiImplicitParam(value = "ArgSaveParam", name = "param", dataType = "ArgSaveParam", required = true)
    @PutMapping(value = APIPath.ArgPath.$)
    public CommonResult<Arg> update(@RequestBody ArgSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

    @ApiOperation("Get arg")
    @ApiImplicitParam(value = "ArgGetParam", name = "param", dataType = "ArgGetParam", required = true)
    @GetMapping(value = APIPath.ArgPath.GET)
    public CommonResult<Arg> get(ArgGetParam param) {
        return CommonResult.putResult(service.get(param));
    }

    @ApiOperation("List args")
    @ApiImplicitParam(value = "ArgListParam", name = "param", dataType = "ArgListParam", required = true)
    @GetMapping(APIPath.ArgPath.$)
    public CommonResult<Map<String, Object>> list(ArgListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("Remove args")
    @ApiImplicitParam(value = "ArgRemoveParam", name = "param", dataType = "ArgRemoveParam", required = true)
    @DeleteMapping(value = APIPath.ArgPath.$)
    public CommonResult<Void> remove(@RequestBody ArgRemoveParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @ApiOperation("Get sql result")
    @ApiImplicitParam(value = "SqlArgParam", name = "param", dataType = "SqlArgParam", required = true)
    @GetMapping(value = APIPath.ArgPath.GET_SQL_RESULT)
    public CommonResult<List<Map<String, Object>>> getSqlResult(SqlArgParam param) {
        return CommonResult.putResult(service.getSqlResult(param));
    }

    @ApiOperation("Get trade date")
    @ApiImplicitParam(value = "TradeDateArgParam", name = "param", dataType = "TradeDateArgParam", required = true)
    @GetMapping(value = APIPath.ArgPath.GET_TRADE_DATE)
    public CommonResult<Integer> getTradeDate(TradeDateArgParam param) {
        return CommonResult.putResult(service.getTradeDate(param));
    }

}
