package com.github.attemper.scheduler.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.scheduler.handler.CalendarUpdatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Update calendar in quartz")
@RestController
public class CalendarUpdatedController {

    @Autowired
    private CalendarUpdatedService service;

    @ApiOperation("Update calendar")
    @ApiImplicitParam(value = "CalendarGetParam", name = "param", dataType = "CalendarGetParam", required = true)
    @PostMapping(APIPath.SchedulerPath.CALENDAR)
    public CommonResult<Void> saveCalendar(@RequestBody CalendarGetParam param) {
        return CommonResult.putResult(service.saveCalendar(param));
    }
}
