package com.github.attemper.core.controller.statistics;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.core.service.statistics.CountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("Count")
@RestController
public class CountController {

    @Autowired
    private CountService service;

    @GetMapping(APIPath.CountPath.TENANT)
    @ApiOperation("Get tenant's count")
    public CommonResult<Integer> getTenantCount() {
        return CommonResult.putResult(service.getTenantCount());
    }

    @GetMapping(APIPath.CountPath.JOB)
    @ApiOperation("Get job's count by status")
    public CommonResult<List<Map<String, Object>>> getJobCount() {
        return CommonResult.putResult(service.getJobCount());
    }

    @GetMapping(APIPath.CountPath.INSTANCE)
    @ApiOperation("Get job instance's count by status")
    public CommonResult<List<Map<String, Object>>> getJobInstanceCount() {
        return CommonResult.putResult(service.getJobInstanceCount());
    }

}
