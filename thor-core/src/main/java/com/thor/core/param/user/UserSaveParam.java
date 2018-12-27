package com.thor.core.param.user;

import com.thor.common.constant.RegexConstants;
import com.thor.common.param.CommonParam;
import com.thor.common.util.NumberUtil;
import com.thor.core.entity.User;
import com.thor.core.enums.UserStatus;
import com.xiaoleilu.hutool.util.ReUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;

/**
 * @author ldang
 */
@Data
public class UserSaveParam implements CommonParam {

    @NotBlank(message = "5200")
    @ApiModelProperty(value = "用户名", dataType = "String", position = 1)
    private String userName;

    @NotBlank(message = "5203")
    @ApiModelProperty(value = "中文名(全名)", dataType = "String", position = 5)
    private String displayName;

    @ApiModelProperty(value = "密码", dataType = "String", position = 10)
    private String password;

    @ApiModelProperty(value = "邮箱", dataType = "String", position = 15)
    private String email;

    @ApiModelProperty(value = "手机", dataType = "String", position = 20)
    private String mobile;

    @ApiModelProperty(value = "状态", dataType = "Integer", position = 25)
    private Integer status;

    public User toUser() {
        return User.builder()
                .userName(userName)
                .displayName(displayName)
                .password(password)
                .email(email)
                .mobile(mobile)
                .status(status)
                .build();
    }

    @Override
    public String validate() {
        if(StringUtils.isNotBlank(email)){
            if(!ReUtil.isMatch(RegexConstants.EMAIL, email)){
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

    @Override
    public void preHandle() {
        if(mobile != null){
            mobile = mobile.trim();
        }
    }
}
