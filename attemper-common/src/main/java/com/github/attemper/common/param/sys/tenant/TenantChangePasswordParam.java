package com.github.attemper.common.param.sys.tenant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class TenantChangePasswordParam extends TenantNameParam {

    protected String oldPassword;

    protected String newPassword;

    protected String doubleNewPassword;

    @Override
    public String validate() {
        if (StringUtils.isBlank(oldPassword)
                || StringUtils.isBlank(newPassword) || StringUtils.isBlank(doubleNewPassword)) {
            return "1522";
        }
        return super.validate();
    }
}
