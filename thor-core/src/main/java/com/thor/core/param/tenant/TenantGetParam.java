package com.thor.core.param.tenant;

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
public class TenantGetParam implements CommonParam {

    @NotBlank(message = "5100")
    @ApiModelProperty(value = "租户编号", dataType = "String", position = 5)
    private String id;

}
