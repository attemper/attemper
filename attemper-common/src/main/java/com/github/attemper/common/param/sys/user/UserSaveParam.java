package com.github.attemper.common.param.sys.user;

import com.github.attemper.common.enums.UserStatus;
import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.UserStatus;
import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

/**
 * @author ldang
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveParam implements CommonParam {

    protected String userName;

    protected String displayName;

    protected String password;

    protected String email;

    protected String mobile;

    protected Integer status;

    protected static Pattern emailPattern;

    public String validate() {
        if (StringUtils.isBlank(userName)) {
            return "5200";
        }
        if (StringUtils.isBlank(displayName)) {
            return "5203";
        }
        if(StringUtils.isNotBlank(email)){
            if(emailPattern == null) {
                emailPattern = Pattern.compile(CommonConstants.REGEX_EMAIL);
            }
            if(!emailPattern.matcher(email).find()) {
                return "5215";
            }
        }
        if(StringUtils.isNotBlank(mobile)){
            if(mobile.length() != 11 || !NumberUtil.isPositiveNumber(mobile)){
                return "5218";
            }
        }
        if(UserStatus.get(status) == null){
            return "5221";
        }
        return null;
    }

    public void preHandle() {
        if(mobile != null){
            mobile = mobile.trim();
        }
    }

}
