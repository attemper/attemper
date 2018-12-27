package com.thor.core.param.tenant;

import com.thor.common.param.CommonParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Data
public class TenantRemoveParam implements CommonParam {

    @ApiModelProperty(value = "租户编号数组", dataType = "String[]", position = 1)
    private String[] ids;

    @Override
    public String validate() {
        if(ids == null || ids.length == 0 || StringUtils.isBlank(ids[0])){
            return "5112";
        }
        return null;
    }
}
