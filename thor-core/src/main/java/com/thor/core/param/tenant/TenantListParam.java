package com.thor.core.param.tenant;

import com.thor.common.param.PageSortParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TenantListParam extends PageSortParam {

    @ApiModelProperty(value = "租户编号(like)", dataType = "String", position = 1)
    private String id;

    @ApiModelProperty(value = "租户名称(like)", dataType = "String", position = 5)
    private String name;
}
