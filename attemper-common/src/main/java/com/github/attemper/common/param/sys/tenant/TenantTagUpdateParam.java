package com.github.attemper.common.param.sys.tenant;

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
public class TenantTagUpdateParam implements CommonParam {

    protected String userName;

    protected List<String> tagNames;

    public String validate() {
        if (StringUtils.isBlank(userName)) {
            return "5100";
        }
        return null;
    }

}
