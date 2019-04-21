package com.sse.attemper.core.controller.project;

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
 * @author ldang
 */
@Api("Project")
@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService service;

	@ApiOperation("Get project tree list")
	@ApiImplicitParam(value = "EmptyParam", name = "param", dataType = "EmptyParam", required = true)
	@GetMapping(APIPath.ProjectPath.TREE_LIST)
	public CommonResult<List<Project>> getTreeList(EmptyParam param) {
		return CommonResult.putResult(service.getAll(param));
	}

	@ApiOperation("Add or update project")
	@ApiImplicitParam(value = "ProjectSaveParam", name = "param", dataType = "ProjectSaveParam", required = true)
	@PostMapping(APIPath.ProjectPath.SAVE)
	public CommonResult<Project> save(@RequestBody ProjectSaveParam param) {
		return CommonResult.putResult(service.save(param));
	}

	@ApiOperation("Remove projects")
	@ApiImplicitParam(value = "ProjectRemoveParam", name = "param", dataType = "ProjectRemoveParam", required = true)
	@DeleteMapping(APIPath.ProjectPath.REMOVE)
	public CommonResult<Void> remove(@RequestBody ProjectRemoveParam param) {
		return CommonResult.putResult(service.remove(param));
    }

	@ApiOperation("Save project info")
	@ApiImplicitParam(value = "ProjectInfoSaveParam", name = "param", dataType = "ProjectInfoSaveParam", required = true)
	@PostMapping(APIPath.ProjectPath.SAVE_INFO)
	public CommonResult<Void> saveInfo(@RequestBody ProjectInfoSaveParam param) {
		return CommonResult.putResult(service.saveInfo(param));
	}

	@ApiOperation("List project info")
	@ApiImplicitParam(value = "ProjectGetParam", name = "param", dataType = "ProjectGetParam", required = true)
	@GetMapping(APIPath.ProjectPath.LIST_INFOS)
	public CommonResult<List<ProjectInfo>> listInfos(ProjectGetParam param) {
		return CommonResult.putResult(service.listInfos(param));
	}

	@ApiOperation("Remove project info")
	@ApiImplicitParam(value = "ProjectInfoRemoveParam", name = "param", dataType = "ProjectInfoRemoveParam", required = true)
	@DeleteMapping(APIPath.ProjectPath.REMOVE_INFO)
	public CommonResult<Void> removeInfo(@RequestBody ProjectInfoRemoveParam param) {
		return CommonResult.putResult(service.removeInfo(param));
	}
}
