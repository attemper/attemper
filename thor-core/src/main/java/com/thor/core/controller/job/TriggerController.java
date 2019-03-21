package com.thor.core.controller.job;

import com.thor.core.service.job.TriggerService;
import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.dispatch.trigger.TriggerGetParam;
import com.thor.sdk.common.param.dispatch.trigger.TriggerUpdateParam;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sdk.common.result.dispatch.trigger.TriggerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @author ldang
 */
@Api(tags = ThorAPIConst.APITag.TRIGGER)
@RestController
public class TriggerController {
	
	@Autowired
	private TriggerService service;

	@ApiOperation(ThorAPIConst.APIOperation.JobTitle.TriggerTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TriggerUpdateParam", required = true)
	@PutMapping(ThorAPIPath.JobPath.TriggerPath.UPDATE)
	public CommonResult<Void> update(@RequestBody TriggerUpdateParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(ThorAPIConst.APIOperation.JobTitle.TriggerTitle.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TriggerGetParam", required = true)
	@GetMapping(ThorAPIPath.JobPath.TriggerPath.GET)
	public CommonResult<TriggerResult> get(TriggerGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

}
