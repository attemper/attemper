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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name ="Calendar")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService service;

    @Autowired
    private CalendarOperatedService calendarOperatedService;

    @Operation(summary = "List calendars")
    @GetMapping(APIPath.CalendarPath.$)
    public CommonResult<List<CalendarInfo>> list() {
        return CommonResult.putResult(service.list());
    }

    @Operation(summary = "Get days of calendar", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(value = APIPath.CalendarPath.DAY)
    public CommonResult<List<DayCalendarConfig>> listDay(DayCalendarListParam param) {
        return CommonResult.putResult(service.listDay(param));
    }

    @Operation(summary = "Save day of calendar", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(APIPath.CalendarPath.DAY)
    public CommonResult<Void> saveDay(@RequestBody DayCalendarSaveParam param) {
        return CommonResult.putResult(calendarOperatedService.saveDay(param));
    }

    @Operation(summary = "Remove day of calendar", parameters = {@Parameter(name = "param", required = true)})
    @DeleteMapping(value = APIPath.CalendarPath.DAY)
    public CommonResult<Void> removeDay(@RequestBody DayCalendarRemoveParam param) {
        return CommonResult.putResult(calendarOperatedService.removeDay(param));
    }

    @Operation(summary = "Import date", parameters = {@Parameter(name = "calendarName", required = true), @Parameter(name = "file", required = true)})
    @PostMapping(APIPath.CalendarPath.IMPORT)
    public CommonResult<Void> importDate(String calendarName, MultipartFile file) {
        return CommonResult.putResult(calendarOperatedService.importDate(calendarName, file));
    }

}
