package com.github.attemper.common.param.sys.tenant;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@ToString
public class TenantGetParam implements CommonParam {

    protected String userName;

    public String validate() {
        if (StringUtils.isBlank(userName)) {
            return "5100";
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public TenantGetParam setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
