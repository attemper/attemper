package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarListParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarRemoveParam;
import com.github.attemper.common.param.dispatch.calendar.DayCalendarSaveParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.calendar.DayCalendarConfig;
import com.github.attemper.core.service.dispatch.CalendarService;
import com.github.attemper.web.service.dispatch.CalendarOperatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api("Calendar")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService service;

    @Autowired
    private CalendarOperatedService calendarOperatedService;

    @ApiOperation("List calendars")
    @GetMapping(APIPath.CalendarPath.$)
    public CommonResult<List<CalendarInfo>> list() {
        return CommonResult.putResult(service.list());
    }

    @ApiOperation("Get days of calendar")
    @ApiImplicitParam(value = "DayCalendarListParam", name = "param", dataType = "DayCalendarListParam", required = true)
    @GetMapping(value = APIPath.CalendarPath.DAY)
    public CommonResult<List<DayCalendarConfig>> listDay(DayCalendarListParam param) {
        return CommonResult.putResult(service.listDay(param));
    }

    @ApiOperation("Save day of calendar")
    @ApiImplicitParam(value = "DayCalendarSaveParam", name = "param", dataType = "DayCalendarSaveParam", required = true)
    @PostMapping(APIPath.CalendarPath.DAY)
    public CommonResult<Void> saveDay(@RequestBody DayCalendarSaveParam param) {
        return CommonResult.putResult(calendarOperatedService.saveDay(param));
    }

    @ApiOperation("Remove day of calendar")
    @ApiImplicitParam(value = "DayCalendarRemoveParam", name = "param", dataType = "DayCalendarRemoveParam", required = true)
    @DeleteMapping(value = APIPath.CalendarPath.DAY)
    public CommonResult<Void> removeDay(@RequestBody DayCalendarRemoveParam param) {
        return CommonResult.putResult(calendarOperatedService.removeDay(param));
    }

    @ApiOperation("Import date")
    @ApiImplicitParam(value = "MultipartFile", name = "file", dataType = "MultipartFile", required = true)
    @PostMapping(APIPath.CalendarPath.IMPORT)
    public CommonResult<Void> importDate(String calendarName, MultipartFile file) {
        return CommonResult.putResult(calendarOperatedService.importDate(calendarName, file));
    }

}
