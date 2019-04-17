package com.sse.attemper.core.controller.project;

import com.sse.attemper.common.constant.APIConst;
import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.EmptyParam;
import com.sse.attemper.common.param.dispatch.project.*;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.common.result.dispatch.project.ProjectInfo;
import com.sse.attemper.core.service.project.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 
 * @auth ldang
 */
@Api(tags = APIConst.APITag.PROJECT)
@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService service;

	@ApiOperation(APIConst.APIOperation.ProjectTitle.TREE_LIST)
	@ApiImplicitParam(value = "empty param", name = "emptyParam", dataType = "EmptyParam", required = true)
	@GetMapping(APIPath.ProjectPath.TREE_LIST)
    public CommonResult<List<Project>> getTreeList(EmptyParam emptyParam) {
        return CommonResult.putResult(service.getAll(emptyParam));
	}

	@ApiOperation(APIConst.APIOperation.ProjectTitle.SAVE)
	@ApiImplicitParam(value = "被保存的数据", name = "saveParam", dataType = "ProjectSaveParam", required = true)
	@PostMapping(APIPath.ProjectPath.SAVE)
	public CommonResult<Project> save(@RequestBody ProjectSaveParam saveParam) {
        return CommonResult.putResult(service.save(saveParam));
	}

	@ApiOperation(APIConst.APIOperation.ProjectTitle.REMOVE)
    @ApiImplicitParam(value = "被删除的主键数组", name = "removeParam", dataType = "ProjectRemoveParam", required = true)
	@DeleteMapping(APIPath.ProjectPath.REMOVE)
    public CommonResult<Void> remove(@RequestBody ProjectRemoveParam removeParam) {
        service.remove(removeParam);
        return CommonResult.ok();
    }
    
	@ApiOperation(APIConst.APIOperation.ProjectTitle.SAVE_INFO)
	@ApiImplicitParam(value = "save data", name = "param", dataType = "ProjectInfoSaveParam", required = true)
	@PostMapping(APIPath.ProjectPath.SAVE_INFO)
	public CommonResult<Void> saveInfo(@RequestBody ProjectInfoSaveParam param) {
		return CommonResult.putResult(service.saveInfo(param));
	}

	@ApiOperation(APIConst.APIOperation.ProjectTitle.LIST_INFOS)
	@ApiImplicitParam(value = "查询参数", name = "param", dataType = "ProjectGetParam", required = true)
	@GetMapping(APIPath.ProjectPath.LIST_INFOS)
	public CommonResult<List<ProjectInfo>> listInfos(ProjectGetParam param) {
		return CommonResult.putResult(service.listInfos(param));
	}

	@ApiOperation(APIConst.APIOperation.ProjectTitle.REMOVE_INFO)
	@ApiImplicitParam(value = "remove by tenantId and baseUrl", name = "param", dataType = "ProjectInfoRemoveParam", required = true)
	@DeleteMapping(APIPath.ProjectPath.REMOVE_INFO)
	public CommonResult<Void> removeInfo(@RequestBody ProjectInfoRemoveParam param) {
		return CommonResult.putResult(service.removeInfo(param));
	}
}
