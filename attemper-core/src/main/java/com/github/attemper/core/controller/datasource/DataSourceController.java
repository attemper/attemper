package com.github.attemper.core.controller.datasource;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.datasource.DataSourceGetParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceListParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceSaveParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.datasource.ConnectionTestResult;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import com.github.attemper.core.service.datasource.DataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("DataSource")
@RestController
public class DataSourceController {

    @Autowired
    private DataSourceService service;

    @ApiOperation("Add dataSource")
    @ApiImplicitParam(value = "DataSourceSaveParam", name = "param", dataType = "DataSourceSaveParam", required = true)
    @PostMapping(value = APIPath.DataSourcePath.ADD)
    public CommonResult<DataSourceInfo> add(@RequestBody DataSourceSaveParam param) {
        return CommonResult.putResult(service.add(param));
    }

    @ApiOperation("Update dataSource")
    @ApiImplicitParam(value = "DataSourceSaveParam", name = "param", dataType = "DataSourceSaveParam", required = true)
    @PutMapping(value = APIPath.DataSourcePath.UPDATE)
    public CommonResult<DataSourceInfo> update(@RequestBody DataSourceSaveParam param) {
        return CommonResult.putResult(service.update(param));
    }

    @ApiOperation("Get dataSource")
    @ApiImplicitParam(value = "DataSourceGetParam", name = "param", dataType = "DataSourceGetParam", required = true)
    @GetMapping(value = APIPath.DataSourcePath.GET)
    public CommonResult<DataSourceInfo> get(DataSourceGetParam param) {
        return CommonResult.putResult(service.get(param));
    }

    @ApiOperation("List dataSources")
    @ApiImplicitParam(value = "DataSourceListParam", name = "param", dataType = "DataSourceListParam", required = true)
    @GetMapping(APIPath.DataSourcePath.LIST)
    public CommonResult<Map<String, Object>> list(DataSourceListParam param) {
        return CommonResult.putResult(service.list(param));
    }

    @ApiOperation("Remove dataSources")
    @ApiImplicitParam(value = "DataSourceNamesParam", name = "param", dataType = "DataSourceNamesParam", required = true)
    @DeleteMapping(value = APIPath.DataSourcePath.REMOVE)
    public CommonResult<Void> remove(@RequestBody DataSourceNamesParam param) {
        return CommonResult.putResult(service.remove(param));
    }

    @ApiOperation("test connections of dataSources")
    @ApiImplicitParam(value = "DataSourceNamesParam", name = "param", dataType = "DataSourceNamesParam", required = true)
    @PostMapping(value = APIPath.DataSourcePath.TEST_CONNECTION)
    public CommonResult<List<ConnectionTestResult>> testConnection(@RequestBody DataSourceNamesParam param) {
        return CommonResult.putResult(service.testConnection(param));
    }

}
