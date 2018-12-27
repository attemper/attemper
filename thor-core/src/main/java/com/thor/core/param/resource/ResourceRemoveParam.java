package com.thor.core.param.resource;

import com.thor.common.param.CommonParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Data
public class ResourceRemoveParam implements CommonParam {

    @ApiModelProperty(value = "资源名称数组", dataType = "String[]", position = 1)
    private String[] resourceNames;

    @Override
    public String validate() {
        if(resourceNames == null || resourceNames.length == 0 || StringUtils.isBlank(resourceNames[0])){
            return "5400";
        }
        return null;
    }
}
