package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.arg.ArgListParam;
import com.github.attemper.common.param.dispatch.arg.ArgNameParam;
import com.github.attemper.common.param.dispatch.arg.ArgNamesParam;
import com.github.attemper.common.param.dispatch.arg.ArgSaveParam;
import com.github.attemper.common.param.dispatch.arg.ext.SqlArgParam;
import com.github.attemper.common.param.dispatch.arg.ext.TradeDateArgParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.core.service.dispatch.ArgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name ="Arg")
@RestController
public class ArgController {

    @Autowired
    private ArgService service;

    @Operation(summary = "Add arg", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(value = APIPath.ArgPath.$)
    public CommonResult<Arg> add(@RequestBody ArgSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @Operation(summary = "Update arg", parameters = {@Parameter(name = "param", required = true)})
    @PutMapping(value = APIPath.ArgPath.$)
    public CommonResult<Arg> update(@RequestBody ArgSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

    @Operation(summary = "Get arg", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(value = APIPath.ArgPath.GET)
    public CommonResult<Arg> get(ArgNameParam param) {
        return CommonResult.putResult(service.get(param));
    }

    @Operation(summary = "List args", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.ArgPath.$)
    public CommonResult<Map<String, Object>> list(ArgListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @Operation(summary = "Remove args", parameters = {@Parameter(name = "param", required = true)})
    @DeleteMapping(value = APIPath.ArgPath.$)
    public CommonResult<Void> remove(@RequestBody ArgNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @Operation(summary = "Get sql result", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(value = APIPath.ArgPath.GET_SQL_RESULT)
    public CommonResult<List<Map<String, Object>>> getSqlResult(SqlArgParam param) {
        return CommonResult.putResult(service.getSqlResult(param));
    }

    @Operation(summary = "Get trade date", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(value = APIPath.ArgPath.GET_TRADE_DATE)
    public CommonResult<Integer> getTradeDate(TradeDateArgParam param) {
        return CommonResult.putResult(service.getTradeDate(param));
    }

}
