package com.sse.attemper.common.param.sys.tenant;

import com.sse.attemper.common.param.CommonParam;
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
public class TenantRemoveParam implements CommonParam {

    protected List<String> ids;

    public String validate() {
        if (ids == null || ids.isEmpty() || StringUtils.isBlank(ids.get(0))) {
            return "5112";
        }
        return null;
    }

}
