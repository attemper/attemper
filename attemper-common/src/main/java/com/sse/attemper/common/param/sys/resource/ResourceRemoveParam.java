package com.sse.attemper.common.param.sys.resource;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRemoveParam implements CommonParam {

    protected String[] resourceNames;

    public String validate() {
        if(resourceNames == null || resourceNames.length == 0 || StringUtils.isBlank(resourceNames[0])){
            return "5400";
        }
        return null;
    }

}
