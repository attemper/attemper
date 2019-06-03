package com.github.attemper.common.param.sys.tenant;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author ldang
 */
@Data
public class TenantRemoveParam implements CommonParam {

    protected List<String> userNames;

    public String validate() {
        if (userNames == null || userNames.isEmpty() || StringUtils.isBlank(userNames.get(0))) {
            return "5112";
        }
        return null;
    }

}
