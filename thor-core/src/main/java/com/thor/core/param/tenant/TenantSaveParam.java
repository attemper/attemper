package com.thor.core.param.tenant;

import com.thor.common.param.CommonParam;
import com.thor.core.entity.Tenant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ldang
 */
@Data
public class TenantSaveParam implements CommonParam {

    @NotBlank(message = "5100")
    @ApiModelProperty(value = "租户编号", dataType = "String", position = 1)
    private String id;

    @NotBlank(message = "5103")
    @ApiModelProperty(value = "租户名称", dataType = "String", position = 5)
    private String name;

    @NotBlank(message = "5106")
    @ApiModelProperty(value = "管理员", dataType = "String", position = 10)
    private String admin;

    public Tenant toTenant() {
        return Tenant.builder()
                .id(id)
                .name(name)
                .admin(admin)
                .build();
    }

}
