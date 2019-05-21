package com.github.attemper.core.controller.job;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerNameParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.core.service.job.TriggerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 
 * @author ldang
 */
@Api("Trigger")
@RestController
public class TriggerController {
	
	@Autowired
	private TriggerService service;

	@ApiOperation("Get trigger info by job")
	@ApiImplicitParam(value = "TriggerGetParam", name = "param", dataType = "TriggerGetParam", required = true)
	@GetMapping(APIPath.TriggerPath.GET)
	public CommonResult<TriggerResult> get(TriggerGetParam param) {
		return CommonResult.putResult(service.get(param));
	}

	@ApiOperation("Get calendars by a trigger")
	@ApiImplicitParam(value = "TriggerNameParam", name = "param", dataType = "TriggerNameParam", required = true)
	@GetMapping(APIPath.TriggerPath.GET_CALENDAR)
	public CommonResult<List<CalendarInfo>> getCalendars(TriggerNameParam param) {
		return CommonResult.putResult(service.getCalendars(param));
	}
}
