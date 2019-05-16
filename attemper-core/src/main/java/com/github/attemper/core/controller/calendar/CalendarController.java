package com.github.attemper.core.controller.calendar;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.calendar.CalendarGetParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarConfigRemoveParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarConfigSaveParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import com.github.attemper.core.service.calendar.CalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("Calendar")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService service;

    @ApiOperation("List calendars")
    @GetMapping(APIPath.CalendarPath.LIST)
    public CommonResult<List<CalendarInfo>> list() {
        return CommonResult.putResult(service.list());
    }

    @ApiOperation("Save day of calendar")
    @ApiImplicitParam(value = "DayCalendarConfigSaveParam", name = "param", dataType = "DayCalendarConfigSaveParam", required = true)
    @PostMapping(APIPath.CalendarPath.SAVE_DAY)
    public CommonResult<Void> saveDay(@RequestBody DayCalendarConfigSaveParam param) {
        return CommonResult.putResult(service.saveDay(param));
    }

    @ApiOperation("Get days of calendar")
    @ApiImplicitParam(value = "CalendarGetParam", name = "param", dataType = "CalendarGetParam", required = true)
    @GetMapping(value = APIPath.CalendarPath.LIST_DAY)
    public CommonResult<List<DayCalendarConfig>> listDay(CalendarGetParam param) {
        return CommonResult.putResult(service.listDay(param));
    }

    @ApiOperation("Remove day of calendar")
    @ApiImplicitParam(value = "DayCalendarConfigRemoveParam", name = "param", dataType = "DayCalendarConfigRemoveParam", required = true)
    @DeleteMapping(value = APIPath.CalendarPath.REMOVE_DAY)
    public CommonResult<Void> removeDay(@RequestBody DayCalendarConfigRemoveParam param) {
        return CommonResult.putResult(service.removeDay(param));
    }
}
