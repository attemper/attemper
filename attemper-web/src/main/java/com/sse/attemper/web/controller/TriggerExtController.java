package com.sse.attemper.web.controller;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.dispatch.trigger.TriggerUpdateParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.web.service.job.TriggerExtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @author ldang
 */
@Api(tags = APIConst.APITag.TRIGGER)
@RestController
public class TriggerExtController {
	
	@Autowired
	private TriggerExtService service;

	@ApiOperation(APIConst.APIOperation.JobTitle.TriggerTitle.UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "saveParam", dataType = "TriggerUpdateParam", required = true)
	@PutMapping(APIPath.JobPath.TriggerPath.UPDATE)
	public CommonResult<Void> update(@RequestBody TriggerUpdateParam saveParam) {
		return CommonResult.putResult(service.update(saveParam));
	}

}
