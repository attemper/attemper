package com.github.attemper.common.param.sys.user;

import com.github.attemper.common.param.CommonParam;
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
public class UserGetParam implements CommonParam {

    protected String userName;

    public String validate() {
        if (StringUtils.isBlank(userName)) {
            return "5200";
        }
        return null;
    }

}
