package com.sse.attemper.common.param.sys.login;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam implements CommonParam {

    protected String userName;

    protected String password;

    public String validate() {
        if(StringUtils.isBlank(userName)){
            return "5000";
        }

        if(StringUtils.isBlank(password)) {
            return "5003";
        }

        return null;
    }

}
