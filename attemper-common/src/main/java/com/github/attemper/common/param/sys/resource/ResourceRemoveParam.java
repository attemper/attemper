package com.github.attemper.common.param.sys.resource;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRemoveParam implements CommonParam {

    protected List<String> resourceNames;

    public String validate() {
        if (resourceNames == null || resourceNames.isEmpty() || StringUtils.isBlank(resourceNames.get(0))) {
            return "5400";
        }
        return null;
    }

}
