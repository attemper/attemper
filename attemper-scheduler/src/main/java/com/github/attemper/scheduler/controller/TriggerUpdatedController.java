package com.github.attemper.scheduler.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.scheduler.TriggerChangedParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.scheduler.handler.TriggerUpdatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Update trigger in quartz")
@RestController
public class TriggerUpdatedController {

    @Autowired
    private TriggerUpdatedService service;

    @ApiOperation("Update trigger")
    @ApiImplicitParam(value = "TriggerChangedParam", name = "param", dataType = "TriggerChangedParam", required = true)
    @PostMapping(APIPath.SchedulerPath.TRIGGER)
    public CommonResult<Void> updateTrigger(@RequestBody TriggerChangedParam param) {
        return CommonResult.putResult(service.updateTrigger(param));
    }
}
