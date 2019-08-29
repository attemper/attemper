package com.github.attemper.executor.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.executor.service.DataSourceUpdatedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Operate datasource")
@RestController
public class DataSourceUpdatedController {

    @Autowired
    private DataSourceUpdatedService service;

    @ApiOperation("remove data source from store")
    @ApiImplicitParam(value = "DataSourceNamesParam", name = "param", dataType = "DataSourceNamesParam", required = true)
    @PostMapping(APIPath.ExecutorPath.REMOVE_DATA_SOURCE)
    public CommonResult<Void> remove(@RequestBody DataSourceNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

}
