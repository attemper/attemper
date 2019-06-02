package com.github.attemper.core.controller.calendar;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.calendar.CalendarListParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarListParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarRemoveParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarSaveParam;
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
    @ApiImplicitParam(value = "CalendarListParam", name = "param", dataType = "CalendarListParam", required = true)
    @GetMapping(APIPath.CalendarPath.LIST)
    public CommonResult<List<CalendarInfo>> list(CalendarListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("Save day of calendar")
    @ApiImplicitParam(value = "DayCalendarSaveParam", name = "param", dataType = "DayCalendarSaveParam", required = true)
    @PostMapping(APIPath.CalendarPath.SAVE_DAY)
    public CommonResult<Void> saveDay(@RequestBody DayCalendarSaveParam param) {
        return CommonResult.putResult(service.saveDay(param));
    }

    @ApiOperation("Get days of calendar")
    @ApiImplicitParam(value = "DayCalendarListParam", name = "param", dataType = "DayCalendarListParam", required = true)
    @GetMapping(value = APIPath.CalendarPath.LIST_DAY)
    public CommonResult<List<DayCalendarConfig>> listDay(DayCalendarListParam param) {
        return CommonResult.putResult(service.listDay(param));
    }

    @ApiOperation("Remove day of calendar")
    @ApiImplicitParam(value = "DayCalendarRemoveParam", name = "param", dataType = "DayCalendarRemoveParam", required = true)
    @DeleteMapping(value = APIPath.CalendarPath.REMOVE_DAY)
    public CommonResult<Void> removeDay(@RequestBody DayCalendarRemoveParam param) {
        return CommonResult.putResult(service.removeDay(param));
    }
}
