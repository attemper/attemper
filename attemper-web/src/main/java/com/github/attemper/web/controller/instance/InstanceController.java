package com.github.attemper.web.controller.instance;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdsParam;
import com.github.attemper.common.param.dispatch.instance.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.common.result.dispatch.instance.InstanceWithChildren;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.web.service.instance.InstanceOperatedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name ="Instance")
@RestController
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private InstanceOperatedService instanceOperatedService;

    @Operation(summary = "List job instances", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.InstancePath.$)
    public CommonResult<Map<String, Object>> listMonitor(InstanceListParam param) {
        return CommonResult.putResult(instanceService.listMonitor(param));
    }

    @Operation(summary = "List job instances children", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.InstancePath.CHILDREN)
    public CommonResult<List<InstanceWithChildren>> listMonitor(InstanceGetParam param) {
        return CommonResult.putResult(instanceService.listMonitorChildren(param));
    }

    @Operation(summary = "List retried job instances", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.InstancePath.RETRY)
    public CommonResult<Map<String, Object>> listRetry(InstanceListParam param) {
        return CommonResult.putResult(instanceService.listRetry(param));
    }

    @Operation(summary = "List retried job instances children", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.InstancePath.RETRY_CHILDREN)
    public CommonResult<List<InstanceWithChildren>> listRetry(InstanceGetParam param) {
        return CommonResult.putResult(instanceService.listRetriedChildren(param));
    }

    @Operation(summary = "List all activity nodes of one instance", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.InstancePath.ACT)
    public CommonResult<List<InstanceAct>> listAct(InstanceActParam param) {
        return CommonResult.putResult(instanceService.listAct(param));
    }

    @Operation(summary = "Args of one instance", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.InstancePath.ARG)
    public CommonResult<String> getInstanceArgs(InstanceIdParam param) {
        return CommonResult.putResult(instanceOperatedService.getInstanceArgs(param));
    }

    @Operation(summary = "Retry", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(value = APIPath.InstancePath.RETRY)
    public CommonResult<Void> retry(@RequestBody InstanceJsonArgParam param) {
        return CommonResult.putResult(instanceOperatedService.retry(param));
    }

    @Operation(summary = "Terminate", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(value = APIPath.InstancePath.TERMINATE)
    public CommonResult<Void> terminate(@RequestBody IdsParam param) {
        return CommonResult.putResult(instanceOperatedService.terminate(param));
    }

}
