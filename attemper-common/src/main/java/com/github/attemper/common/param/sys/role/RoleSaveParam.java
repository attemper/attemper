package com.github.attemper.common.param.sys.role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class RoleSaveParam extends RoleNameParam {

    protected String displayName;

    protected String remark;

    public String validate() {
        if(StringUtils.isBlank(displayName)) {
            return "5303";
        }
        return super.validate();
    }

}
