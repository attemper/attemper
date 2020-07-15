package com.github.attemper.web.controller.application;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.app.project.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.app.project.ProjectInfo;
import com.github.attemper.core.service.application.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project")
@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService service;

	@Operation(summary = "Get project tree list")
	@GetMapping(APIPath.ProjectPath.LIST_TREE)
	public CommonResult<List<Project>> getTreeList() {
		return CommonResult.putResult(service.getAll());
	}

	@Operation(summary = "Add or update project", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.ProjectPath.$)
	public CommonResult<Project> save(@RequestBody ProjectSaveParam param) {
		return CommonResult.putResult(service.save(param));
	}

	@Operation(summary = "Remove projects", parameters = {@Parameter(name = "param", required = true)})
	@DeleteMapping(APIPath.ProjectPath.$)
	public CommonResult<Void> remove(@RequestBody ProjectNamesParam param) {
		return CommonResult.putResult(service.remove(param));
    }

	@Operation(summary = "Save project info", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.ProjectPath.INFO)
	public CommonResult<ProjectInfo> saveInfo(@RequestBody ProjectInfoSaveParam param) {
		return CommonResult.putResult(service.saveInfo(param));
	}

	@Operation(summary = "List project info", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.ProjectPath.INFO)
	public CommonResult<List<ProjectInfo>> listInfo(ProjectNameParam param) {
		return CommonResult.putResult(service.listInfo(param));
	}

	@Operation(summary = "Remove project info", parameters = {@Parameter(name = "param", required = true)})
	@DeleteMapping(APIPath.ProjectPath.INFO)
	public CommonResult<Void> removeInfo(@RequestBody ProjectInfoRemoveParam param) {
		return CommonResult.putResult(service.removeInfo(param));
	}

	@Operation(summary = "Save project executor", parameters = {@Parameter(name = "param", required = true)})
	@PostMapping(APIPath.ProjectPath.EXECUTOR)
	public CommonResult<Void> saveExecutors(@RequestBody ProjectExecutorSaveParam param) {
		return CommonResult.putResult(service.saveExecutors(param));
	}

	@Operation(summary = "List project executor", parameters = {@Parameter(name = "param", required = true)})
	@GetMapping(APIPath.ProjectPath.EXECUTOR)
	public CommonResult<List<String>> listExecutor(ProjectNameParam param) {
		return CommonResult.putResult(service.listExecutor(param));
	}
}
