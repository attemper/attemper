package com.thor.core.controller.job;

import com.thor.core.service.job.AtomJobService;
import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.job.atom.AtomJobConfigUpdateParam;
import com.thor.sdk.common.result.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @auth ldang
 */
@Api(tags = ThorAPIConst.Tag.GROUP)
@RestController
public class AtomJobController {
	
	@Autowired
	private AtomJobService service;


	@ApiOperation(ThorAPIConst.Operation.Job.Atom.JOB_CONFIG_UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "updateParam", dataType = "AtomJobConfigUpdateParam", required = true)
	@PutMapping(ThorAPIPath.Job.Atom.JOB_CONFIG_UPDATE)
	public CommonResult updateAtomJobConfig(@RequestBody AtomJobConfigUpdateParam updateParam) {
		return CommonResult.putResult(service.updateAtomJobConfig(updateParam));
	}
}
