package com.github.attemper.web.controller.dispatch;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.param.dispatch.datasource.DataSourceListParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNameParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceNamesParam;
import com.github.attemper.common.param.dispatch.datasource.DataSourceSaveParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.datasource.ConnectionTestResult;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import com.github.attemper.core.service.dispatch.DataSourceService;
import com.github.attemper.web.service.dispatch.DataSourceOperatedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name ="DataSource")
@RestController
public class DataSourceController {

    @Autowired
    private DataSourceOperatedService dataSourceOperatedService;

    @Autowired
    private DataSourceService dataSourceService;

    @Operation(summary = "Add dataSource", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(value = APIPath.DataSourcePath.$)
    public CommonResult<DataSourceInfo> add(@RequestBody DataSourceSaveParam param) {
        return CommonResult.putResult(dataSourceOperatedService.add(param));
    }

    @Operation(summary = "Update dataSource", parameters = {@Parameter(name = "param", required = true)})
    @PutMapping(value = APIPath.DataSourcePath.$)
    public CommonResult<DataSourceInfo> update(@RequestBody DataSourceSaveParam param) {
        return CommonResult.putResult(dataSourceOperatedService.update(param));
    }

    @Operation(summary = "Get dataSource", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(value = APIPath.DataSourcePath.GET)
    public CommonResult<DataSourceInfo> get(DataSourceNameParam param) {
        return CommonResult.putResult(dataSourceService.get(param));
    }

    @Operation(summary = "List dataSources", parameters = {@Parameter(name = "param", required = true)})
    @GetMapping(APIPath.DataSourcePath.$)
    public CommonResult<Map<String, Object>> list(DataSourceListParam param) {
        return CommonResult.putResult(dataSourceService.list(param));
    }

    @Operation(summary = "Remove dataSources", parameters = {@Parameter(name = "param", required = true)})
    @DeleteMapping(value = APIPath.DataSourcePath.$)
    public CommonResult<Void> remove(@RequestBody DataSourceNamesParam param) {
        return CommonResult.putResult(dataSourceOperatedService.remove(param));
    }

    @Operation(summary = "test connections of dataSources", parameters = {@Parameter(name = "param", required = true)})
    @PostMapping(value = APIPath.DataSourcePath.TEST_CONNECTION)
    public CommonResult<List<ConnectionTestResult>> testConnection(@RequestBody DataSourceNamesParam param) {
        return CommonResult.putResult(dataSourceService.testConnection(param));
    }

}
