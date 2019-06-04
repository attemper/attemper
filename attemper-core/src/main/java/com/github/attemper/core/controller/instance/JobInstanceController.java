package com.github.attemper.core.controller.instance;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.instance.JobInstanceActParam;
import com.github.attemper.common.param.dispatch.instance.JobInstanceListParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.core.service.instance.JobInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("JobInstance")
@RestController
public class JobInstanceController {

    @Autowired
    private JobInstanceService service;

    @ApiOperation("List job instances")
    @ApiImplicitParam(value = "JobInstanceListParam", name = "param", dataType = "JobInstanceListParam", required = true)
    @GetMapping(APIPath.InstancePath.LIST)
    public CommonResult<Map<String, Object>> list(JobInstanceListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("List all activity nodes of one instance")
    @ApiImplicitParam(value = "JobInstanceActParam", name = "param", dataType = "JobInstanceActParam", required = true)
    @GetMapping(APIPath.InstancePath.LIST_ACT)
    public CommonResult<List<JobInstanceAct>> listAct(JobInstanceActParam param) {
        return CommonResult.putResult(service.listAct(param));
    }
}
