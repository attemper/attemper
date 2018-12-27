package com.thor.core.param.tag;

import com.thor.common.param.PageSortParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagListParam extends PageSortParam {

    @ApiModelProperty(value = "标签名称(like)", dataType = "String", position = 1)
    private String tagName;

    @ApiModelProperty(value = "标签名称(like)", dataType = "String", position = 5)
    private String displayName;
}
