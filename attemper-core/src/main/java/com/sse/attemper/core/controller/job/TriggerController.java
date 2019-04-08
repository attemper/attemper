package com.sse.attemper.core.controller.job;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.trigger.TriggerGetParam;
import com.sse.attemper.common.param.dispatch.trigger.TriggerUpdateParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.trigger.TriggerResult;
import com.sse.attemper.core.service.job.TriggerService;
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
@Api(tags = APIConst.APITag.TRIGGER)
@RestController
public class TriggerController {
	
	@Autowired
	private TriggerService service;

	@ApiOperation(APIConst.APIOperation.JobTitle.TriggerTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TriggerUpdateParam", required = true)
	@PutMapping(APIPath.JobPath.TriggerPath.UPDATE)
	public CommonResult<Void> update(@RequestBody TriggerUpdateParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.JobTitle.TriggerTitle.GET)
	@ApiImplicitParam(value = "查询单个对象信息", name = "getParam", dataType = "TriggerGetParam", required = true)
	@GetMapping(APIPath.JobPath.TriggerPath.GET)
	public CommonResult<TriggerResult> get(TriggerGetParam getParam) {
		return CommonResult.putResult(service.get(getParam));
	}

}
