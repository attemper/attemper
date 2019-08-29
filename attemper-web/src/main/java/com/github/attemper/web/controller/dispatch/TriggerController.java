package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerNameParam;
import com.github.attemper.common.param.dispatch.trigger.TriggerSaveParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.calendar.CalendarInfo;
import com.github.attemper.common.result.dispatch.trigger.TriggerResult;
import com.github.attemper.core.service.dispatch.TriggerService;
import com.github.attemper.web.service.dispatch.TriggerOperatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("Trigger")
@RestController
public class TriggerController {
	
	@Autowired
	private TriggerService triggerService;

	@Autowired
	private TriggerOperatedService triggerOperatedService;

	@ApiOperation("Get trigger info by job")
	@ApiImplicitParam(value = "TriggerGetParam", name = "param", dataType = "TriggerGetParam", required = true)
	@GetMapping(APIPath.TriggerPath.GET)
	public CommonResult<TriggerResult> get(TriggerGetParam param) {
		return CommonResult.putResult(triggerService.get(param));
	}

	@ApiOperation("Get calendars by a trigger")
	@ApiImplicitParam(value = "TriggerNameParam", name = "param", dataType = "TriggerNameParam", required = true)
	@GetMapping(APIPath.TriggerPath.GET_CALENDAR)
	public CommonResult<List<CalendarInfo>> getCalendars(TriggerNameParam param) {
		return CommonResult.putResult(triggerService.getCalendars(param));
	}

	@ApiOperation("Update trigger")
	@ApiImplicitParam(value = "TriggerSaveParam", name = "param", dataType = "TriggerSaveParam", required = true)
	@PutMapping(APIPath.TriggerPath.UPDATE)
	public CommonResult<Void> update(@RequestBody TriggerSaveParam param) {
		return CommonResult.putResult(triggerOperatedService.update(param));
	}
}
