package com.sse.attemper.common.param.sys.user;

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
public class UserRemoveParam implements CommonParam {

    protected String[] userNames;

    public String validate() {
        if(userNames == null || userNames.length == 0 || StringUtils.isBlank(userNames[0])){
            return "5200";
        }
        return null;
    }

}
