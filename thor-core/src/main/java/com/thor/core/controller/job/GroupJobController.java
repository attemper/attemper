package com.thor.core.controller.job;

import com.thor.core.service.job.GroupJobService;
import com.thor.sdk.common.constant.ThorAPIConst;
import com.thor.sdk.common.constant.ThorAPIPath;
import com.thor.sdk.common.param.job.group.GroupSubJobRemoveParam;
import com.thor.sdk.common.param.job.group.GroupSubJobUpdateParam;
import com.thor.sdk.common.param.job.group.GroupWithAtomAndSubListParam;
import com.thor.sdk.common.result.CommonResult;
import com.thor.sdk.common.result.job.BaseJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 
 * @auth ldang
 */
@Api(tags = ThorAPIConst.Tag.GROUP)
@RestController
public class GroupJobController {
	
	@Autowired
	private GroupJobService service;

	@ApiOperation(ThorAPIConst.Operation.Job.Group.GROUP_WITH_ATOM_ADN_SUB_LIST)
	@ApiImplicitParam(value = "查询参数", name = "listParam", dataType = "GroupWithAtomAndSubListParam", required = true)
	@GetMapping(ThorAPIPath.Job.Group.GROUP_WITH_ATOM_ADN_SUB_LIST)
	public CommonResult<Map<String, Object>> listAtomAndSubJob(GroupWithAtomAndSubListParam listParam) {
		return CommonResult.putResult(service.listAtomAndSubJob(listParam));
	}


	@ApiOperation(ThorAPIConst.Operation.Job.Group.SUB_JOB_UPDATE)
	@ApiImplicitParam(value = "被更新的数据", name = "updateParam", dataType = "GroupSubJobUpdateParam", required = true)
	@PutMapping(ThorAPIPath.Job.Group.SUB_JOB_UPDATE)
	public CommonResult<BaseJob> updateSubJob(@RequestBody GroupSubJobUpdateParam updateParam) {
		return CommonResult.putResult(service.updateSubJob(updateParam));
	}

	@ApiOperation(ThorAPIConst.Operation.Job.Group.SUB_JOB_REMOVE)
	@ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "GroupSubJobRemoveParam", required = true)
	@DeleteMapping(ThorAPIPath.Job.Group.SUB_JOB_REMOVE)
	public CommonResult removeSubJob(@RequestBody GroupSubJobRemoveParam removeParam) {
		service.removeSubJob(removeParam);
		return CommonResult.ok();
	}
}
