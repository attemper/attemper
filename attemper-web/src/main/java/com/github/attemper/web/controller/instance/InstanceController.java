package com.github.attemper.web.controller.instance;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdsParam;
import com.github.attemper.common.param.dispatch.instance.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.common.result.dispatch.instance.InstanceWithChildren;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.web.service.instance.InstanceOperatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("Instance")
@RestController
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private InstanceOperatedService instanceOperatedService;

    @ApiOperation("List job instances")
    @ApiImplicitParam(value = "InstanceListParam", name = "param", dataType = "InstanceListParam", required = true)
    @GetMapping(APIPath.InstancePath.$)
    public CommonResult<Map<String, Object>> listMonitor(InstanceListParam param) {
        return CommonResult.putResult(instanceService.listMonitor(param));
    }

    @ApiOperation("List job instances children")
    @ApiImplicitParam(value = "InstanceGetParam", name = "param", dataType = "InstanceGetParam", required = true)
    @GetMapping(APIPath.InstancePath.CHILDREN)
    public CommonResult<List<InstanceWithChildren>> listMonitor(InstanceGetParam param) {
        return CommonResult.putResult(instanceService.listMonitorChildren(param));
    }

    @ApiOperation("List retried job instances")
    @ApiImplicitParam(value = "InstanceListParam", name = "param", dataType = "InstanceListParam", required = true)
    @GetMapping(APIPath.InstancePath.RETRY)
    public CommonResult<Map<String, Object>> listRetry(InstanceListParam param) {
        return CommonResult.putResult(instanceService.listRetry(param));
    }

    @ApiOperation("List retried job instances children")
    @ApiImplicitParam(value = "InstanceGetParam", name = "param", dataType = "InstanceGetParam", required = true)
    @GetMapping(APIPath.InstancePath.RETRY_CHILDREN)
    public CommonResult<List<InstanceWithChildren>> listRetry(InstanceGetParam param) {
        return CommonResult.putResult(instanceService.listRetriedChildren(param));
    }

    @ApiOperation("List all activity nodes of one instance")
    @ApiImplicitParam(value = "InstanceActParam", name = "param", dataType = "InstanceActParam", required = true)
    @GetMapping(APIPath.InstancePath.ACT)
    public CommonResult<List<InstanceAct>> listAct(InstanceActParam param) {
        return CommonResult.putResult(instanceService.listAct(param));
    }

    @ApiOperation("Args of one instance")
    @ApiImplicitParam(value = "InstanceIdParam", name = "param", dataType = "InstanceIdParam", required = true)
    @GetMapping(APIPath.InstancePath.ARG)
    public CommonResult<String> getInstanceArgs(InstanceIdParam param) {
        return CommonResult.putResult(instanceOperatedService.getInstanceArgs(param));
    }

    @ApiOperation("Retry")
    @ApiImplicitParam(value = "InstanceJsonArgParam", name = "param", dataType = "InstanceJsonArgParam", required = true)
    @PostMapping(value = APIPath.InstancePath.RETRY)
    public CommonResult<Void> retry(@RequestBody InstanceJsonArgParam param) {
        return CommonResult.putResult(instanceOperatedService.retry(param));
    }

    @ApiOperation("Terminate")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @PostMapping(value = APIPath.InstancePath.TERMINATE)
    public CommonResult<Void> terminate(@RequestBody IdsParam param) {
        return CommonResult.putResult(instanceOperatedService.terminate(param));
    }

}
