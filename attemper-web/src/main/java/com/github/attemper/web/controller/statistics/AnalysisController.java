package com.github.attemper.web.controller.statistics;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.statistics.InstanceDurationParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.statistics.analysis.AppPlan;
import com.github.attemper.web.service.statistics.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name ="Analysis")
@RestController
public class AnalysisController {

    @Autowired
    private AnalysisService service;

    @GetMapping(APIPath.AnalysisPath.PLAN_APP)
    @Operation(summary = "Get all job's plan")
    public CommonResult<List<AppPlan>> getAppPlan() {
        return CommonResult.putResult(service.getAppPlan());
    }

    @GetMapping(APIPath.AnalysisPath.INSTANCE_DURATION)
    @Operation(summary = "Get job instance via duration", parameters = {@Parameter(name = "param", required = true)})
    public CommonResult<Map<String, Object>> getInstanceDuration(InstanceDurationParam param) {
        return CommonResult.putResult(service.getInstanceDuration(param));
    }

}
