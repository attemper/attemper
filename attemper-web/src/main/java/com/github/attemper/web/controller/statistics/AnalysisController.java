package com.github.attemper.web.controller.statistics;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.statistics.InstanceDurationParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.statistics.analysis.AppPlan;
import com.github.attemper.web.service.statistics.AnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("Analysis")
@RestController
public class AnalysisController {

    @Autowired
    private AnalysisService service;

    @GetMapping(APIPath.AnalysisPath.PLAN_APP)
    @ApiOperation("Get all job's plan")
    public CommonResult<List<AppPlan>> getAppPlan() {
        return CommonResult.putResult(service.getAppPlan());
    }

    @GetMapping(APIPath.AnalysisPath.INSTANCE_DURATION)
    @ApiOperation("Get job instance via duration")
    public CommonResult<Map<String, Object>> getInstanceDuration(InstanceDurationParam param) {
        return CommonResult.putResult(service.getInstanceDuration(param));
    }

}
