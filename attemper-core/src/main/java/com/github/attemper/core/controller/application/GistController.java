package com.github.attemper.core.controller.application;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.app.gist.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.app.gist.Gist;
import com.github.attemper.common.result.app.gist.GistInfo;
import com.github.attemper.core.service.application.GistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("Gist")
@RestController
public class GistController {

    @Autowired
    private GistService service;

    @ApiOperation("Add gist")
    @ApiImplicitParam(value = "GistSaveParam", name = "param", dataType = "GistSaveParam", required = true)
    @PostMapping(value = APIPath.GistPath.$)
    public CommonResult<Gist> add(@RequestBody GistSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation("List gists")
    @ApiImplicitParam(value = "GistListParam", name = "param", dataType = "GistListParam", required = true)
    @GetMapping(APIPath.GistPath.$)
    public CommonResult<Map<String, Object>> list(GistListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("Remove gists")
    @ApiImplicitParam(value = "GistNamesParam", name = "param", dataType = "GistNamesParam", required = true)
    @DeleteMapping(value = APIPath.GistPath.$)
    public CommonResult<Void> remove(@RequestBody GistNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @ApiOperation("Add gist info")
    @ApiImplicitParam(value = "GistInfoSaveParam", name = "param", dataType = "GistInfoSaveParam", required = true)
    @PostMapping(value = APIPath.GistPath.INFO)
    public CommonResult<GistInfo> addInfo(@RequestBody GistInfoSaveParam param) {
        return CommonResult.putResult(service.addInfo(param));
    }

    @ApiOperation("List gist info")
    @ApiImplicitParam(value = "GistNameParam", name = "param", dataType = "GistNameParam", required = true)
    @GetMapping(APIPath.GistPath.INFO)
    public CommonResult<List<GistInfo>> listInfo(GistNameParam param) {
        return CommonResult.putResult(service.listInfo(param));
    }

    @ApiOperation("Remove info")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @DeleteMapping(value = APIPath.GistPath.INFO)
    public CommonResult<Void> removeInfo(@RequestBody IdParam param) {
        return CommonResult.putResult(service.removeInfo(param));
    }

    @ApiOperation("Get content")
    @ApiImplicitParam(value = "IdParam", name = "param", dataType = "IdParam", required = true)
    @GetMapping(APIPath.GistPath.CONTENT)
    public CommonResult<String> getContent(IdParam param) {
        return CommonResult.putResult(service.getContent(param));
    }

    @ApiOperation("Update content")
    @ApiImplicitParam(value = "GistInfoContentParam", name = "param", dataType = "GistInfoContentParam", required = true)
    @PutMapping(value = APIPath.GistPath.CONTENT)
    public CommonResult<Void> updateContent(@RequestBody GistInfoContentParam param) {
        return CommonResult.putResult(service.updateContent(param));
    }
}
