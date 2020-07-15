package com.github.attemper.web.controller.application;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.app.gist.*;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.app.gist.Gist;
import com.github.attemper.common.result.app.gist.GistInfo;
import com.github.attemper.core.service.application.GistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Gist")
@RestController
public class GistController {

    @Autowired
    private GistService service;

    @Operation(summary = "Add gist", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(value = APIPath.GistPath.$)
    public CommonResult<Gist> add(@RequestBody GistNameParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @Operation(summary = "List gists", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.GistPath.$)
    public CommonResult<Map<String, Object>> list(GistListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @Operation(summary = "Remove gists", parameters = {@Parameter(name = "param", required = true)})
    @DeleteMapping(value = APIPath.GistPath.$)
    public CommonResult<Void> remove(@RequestBody GistNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @Operation(summary = "Add gist info", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(value = APIPath.GistPath.INFO)
    public CommonResult<GistInfo> addInfo(@RequestBody GistInfoSaveParam param) {
        return CommonResult.putResult(service.addInfo(param));
    }

    @Operation(summary = "List gist info", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.GistPath.INFO)
    public CommonResult<List<GistInfo>> listInfo(GistNameParam param) {
        return CommonResult.putResult(service.listInfo(param));
    }

    @Operation(summary = "Remove info", parameters = {@Parameter(name = "param", required = true)})
    @DeleteMapping(value = APIPath.GistPath.INFO)
    public CommonResult<Void> removeInfo(@RequestBody IdParam param) {
        return CommonResult.putResult(service.removeInfo(param));
    }

    @Operation(summary = "Get content", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.GistPath.CONTENT)
    public CommonResult<String> getContent(IdParam param) {
        return CommonResult.putResult(service.getContent(param));
    }

    @Operation(summary = "Update content", parameters = {@Parameter(name = "param", required = true)})
    @PutMapping(value = APIPath.GistPath.CONTENT)
    public CommonResult<Void> updateContent(@RequestBody GistInfoContentParam param) {
        return CommonResult.putResult(service.updateContent(param));
    }
}
