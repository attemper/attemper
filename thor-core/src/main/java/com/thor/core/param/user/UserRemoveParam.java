package com.thor.core.param.user;

import com.thor.common.param.CommonParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Data
public class UserRemoveParam implements CommonParam {

    @ApiModelProperty(value = "用户名数组", dataType = "String[]", position = 1)
    private String[] userNames;

    @Override
    public String validate() {
        if(userNames == null || userNames.length == 0 || StringUtils.isBlank(userNames[0])){
            return "5200";
        }
        return null;
    }
}
