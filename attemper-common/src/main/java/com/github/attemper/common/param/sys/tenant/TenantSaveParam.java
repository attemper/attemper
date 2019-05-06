package com.github.attemper.common.param.sys.tenant;

import com.github.attemper.common.param.CommonParam;
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
public class TenantSaveParam implements CommonParam {

    protected String id;

    protected String name;

    protected String admin;

    public String validate() {
        if (StringUtils.isBlank(id)) {
            return "5100";
        }
        if (StringUtils.isBlank(name)) {
            return "5103";
        }
        if (StringUtils.isBlank(admin)) {
            return "5106";
        }
        return null;
    }
}
