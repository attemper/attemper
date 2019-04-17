package com.sse.attemper.core.controller.job;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.trigger.TriggerResult;
import com.sse.attemper.core.service.job.TriggerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @author ldang
 */
@Api(tags = APIConst.APITag.TRIGGER)
@RestController
public class TriggerController {
	
	@Autowired
	private TriggerService service;

	@ApiOperation(APIConst.APIOperation.TriggerTitle.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TriggerGetParam", required = true)
	@GetMapping(APIPath.TriggerPath.GET)
	public CommonResult<TriggerResult> get(TriggerGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

}
