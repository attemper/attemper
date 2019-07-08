package com.github.attemper.common.param.sys.tenant;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.TenantStatus;
import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

@Getter
@Setter
@ToString
public class TenantSaveParam implements CommonParam {

    private static Pattern emailPattern;

    protected String userName;

    protected String displayName;

    protected String password;

    protected String email;

    protected String mobile;

    protected Integer status;

    protected String sendConfig;

    public String validate() {
        if (StringUtils.isBlank(userName)) {
            return "5100";
        }
        if (StringUtils.isBlank(password)) {
            return "5101";
        }
        if (StringUtils.isBlank(displayName)) {
            return "5103";
        }
        if (StringUtils.isNotBlank(email)) {
            if (emailPattern == null) {
                emailPattern = Pattern.compile(CommonConstants.REGEX_EMAIL);
            }
            for (String emailStr : email.split(",")) {
                if (!emailPattern.matcher(emailStr).find()) {
                    return "5118";
                }
            }
        }
        if (TenantStatus.get(status) == null) {
            return "5121";
        }
        return null;
    }
}
