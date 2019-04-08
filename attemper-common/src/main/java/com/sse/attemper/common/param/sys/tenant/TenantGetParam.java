package com.sse.attemper.common.param.sys.tenant;

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
public class TenantGetParam implements CommonParam {

    protected String id;

    public String validate() {
        if (StringUtils.isBlank(id)) {
            return "5100";
        }
        return null;
    }

}
