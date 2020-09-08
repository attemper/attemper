package com.github.attemper.common.param.sys.role;

import com.github.attemper.common.param.CommonParam;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
public class RoleNameParam implements CommonParam {

    protected String roleName;

    public String validate() {
        if(StringUtils.isBlank(roleName)) {
            return "5300";
        }
        return null;
    }

    public String getRoleName() {
        return roleName;
    }

    public RoleNameParam setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }
}
