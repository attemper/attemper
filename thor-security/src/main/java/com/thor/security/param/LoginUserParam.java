package com.thor.security.param;

import com.thor.common.param.CommonParam;
import com.thor.core.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;

/**
 * 登录用户参数
 * @author ldang
 */
@Getter
@Setter
public class LoginUserParam implements CommonParam {

    @ApiModelProperty(value = "用户名", dataType = "String", position = 5)
    private String userName;

    @ApiModelProperty(value = "邮箱号", dataType = "String", position = 15)
    private String email;

    @ApiModelProperty(value = "手机号", dataType = "String", position = 20)
    private String mobile;

    @NotBlank(message = "5003")
    @ApiModelProperty(value = "密码", dataType = "String", position = 10)
    private String password;

    public User toUser() {
        return User.builder()
                .userName(userName)
                .password(password)
                .email(email)
                .mobile(mobile)
                .build();
    }

    @Override
    public String validate() {
        if(StringUtils.isBlank(userName) && StringUtils.isBlank(email) && StringUtils.isBlank(mobile)){
            return "5000";
        }

        return null;
    }

    @Override
    public String toString() {
        return
                ", userName=\"" + userName + '\"' +
                ", password=\"" + password + '\"' +
                (email != null ? ", email=\"" + email + "\"" :"") +
                (mobile != null ? ", mobile=\"" + mobile + "\"" : "");
    }
}
