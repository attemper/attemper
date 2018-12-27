package com.thor.core.param.user;

import com.thor.common.param.CommonParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ldang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGetParam implements CommonParam {

    @NotBlank(message = "5200")
    @ApiModelProperty(value = "用户账号", dataType = "String", position = 5)
    private String userName;
}
