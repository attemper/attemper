package com.github.attemper.web.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.IdsParam;
import com.github.attemper.common.param.app.program.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.app.program.CategoryResult;
import com.github.attemper.common.result.app.program.Program;
import com.github.attemper.common.result.app.program.ProgramPackage;
import com.github.attemper.web.service.ProgramOperatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api("Program")
@RestController
public class ProgramController {

    @Autowired
    private ProgramOperatedService service;

    @ApiOperation("Add program")
    @ApiImplicitParam(value = "ProgramSaveParam", name = "param", dataType = "ProgramSaveParam", required = true)
    @PostMapping(value = APIPath.ProgramPath.$)
    public CommonResult<Program> add(@RequestBody ProgramSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation("Update program")
    @ApiImplicitParam(value = "ProgramSaveParam", name = "param", dataType = "ProgramSaveParam", required = true)
    @PutMapping(value = APIPath.ProgramPath.$)
    public CommonResult<Program> update(@RequestBody ProgramSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

    @ApiOperation("Get program")
    @ApiImplicitParam(value = "ProgramNameParam", name = "param", dataType = "ProgramNameParam", required = true)
    @GetMapping(value = APIPath.ProgramPath.GET)
    public CommonResult<Program> get(ProgramNameParam param) {
        return CommonResult.putResult(service.get(param));
    }

    @ApiOperation("List programs")
    @ApiImplicitParam(value = "ProgramListParam", name = "param", dataType = "ProgramListParam", required = true)
    @GetMapping(APIPath.ProgramPath.$)
    public CommonResult<Map<String, Object>> list(ProgramListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("Remove programs")
    @ApiImplicitParam(value = "ProgramNamesParam", name = "param", dataType = "ProgramNamesParam", required = true)
    @DeleteMapping(value = APIPath.ProgramPath.$)
    public CommonResult<Void> remove(@RequestBody ProgramNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @ApiOperation("Upload program package")
    @PostMapping(value = APIPath.ProgramPath.UPLOAD_PACKAGE)
    public CommonResult<ProgramPackage> uploadPackage(String programName, MultipartFile file) {
        return CommonResult.putResult(service.uploadPackage(programName, file));
    }

    @ApiOperation("Export model")
    @GetMapping(APIPath.ProgramPath.DOWNLOAD_PACKAGE)
    public void downloadPackage(HttpServletResponse response, IdParam param) {
        service.downloadPackage(response, param);
    }

    @ApiOperation("Load program package")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @PutMapping(value = APIPath.ProgramPath.LOAD_PACKAGE)
    public CommonResult<ProgramPackage> loadPackage(@RequestBody IdParam param) {
        return CommonResult.putResult(service.loadPackage(param));
    }

    @ApiOperation("Unload program package")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @PutMapping(value = APIPath.ProgramPath.UNLOAD_PACKAGE)
    public CommonResult<ProgramPackage> unloadPackage(@RequestBody IdParam param) {
        return CommonResult.putResult(service.unloadPackage(param));
    }

    @ApiOperation("Get package category")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @GetMapping(value = APIPath.ProgramPath.PACKAGE_CATEGORY)
    public CommonResult<List<CategoryResult>> listPackageCategory(IdParam param) {
        return CommonResult.putResult(service.listPackageCategory(param));
    }

    @ApiOperation("List program packages")
    @ApiImplicitParam(value = "ProgramNameParam", name = "param", dataType = "ProgramNameParam", required = true)
    @GetMapping(APIPath.ProgramPath.PACKAGE)
    public CommonResult<Map<String, Object>> listPackage(ProgramPackageListParam param) {
        return CommonResult.putResult(service.listPackage(param));
    }

    @ApiOperation("Remove program packages")
    @ApiImplicitParam(value = "ProgramNamesParam", name = "param", dataType = "ProgramNamesParam", required = true)
    @DeleteMapping(value = APIPath.ProgramPath.PACKAGE)
    public CommonResult<Void> removePackage(@RequestBody IdsParam param) {
        return CommonResult.putResult(service.removePackage(param));
    }

    @ApiOperation("View file content")
    @GetMapping(APIPath.ProgramPath.VIEW_FILE)
    public CommonResult<String> viewFile(String filePath) {
        return CommonResult.putResult(service.viewFile(filePath));
    }

    @ApiOperation("Download file")
    @GetMapping(APIPath.ProgramPath.DOWNLOAD_FILE)
    public void downloadFile(HttpServletResponse response, String filePath) {
        service.downloadFile(response, filePath);
    }
}
