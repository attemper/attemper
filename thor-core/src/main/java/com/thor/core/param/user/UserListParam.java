package com.thor.core.param.user;

import com.thor.common.param.PageSortParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserListParam extends PageSortParam {

    @ApiModelProperty(value = "用户名(like)", dataType = "String", position = 1)
    private String userName;

    @ApiModelProperty(value = "用户名(like)", dataType = "String", position = 5)
    private String displayName;

    @ApiModelProperty(value = "邮箱号(like)", dataType = "String", position = 15)
    private String email;

    @ApiModelProperty(value = "手机号(like)", dataType = "String", position = 20)
    private String mobile;
}
