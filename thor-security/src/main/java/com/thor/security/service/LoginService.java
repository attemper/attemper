package com.thor.security.service;

import com.thor.common.constant.CommonConstants;
import com.thor.common.enums.ResultStatus;
import com.thor.common.exception.RTException;
import com.thor.config.util.ServletUtil;
import com.thor.core.entity.Resource;
import com.thor.core.entity.Tag;
import com.thor.core.entity.User;
import com.thor.core.enums.UserStatus;
import com.thor.core.param.user.UserGetParam;
import com.thor.core.service.UserService;
import com.thor.security.ext.service.JWTService;
import com.thor.security.result.LoginUserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * service of login
 * @author ldang
 */
@Service
public class LoginService extends BaseSecurityService {

    private @Autowired JWTService jwtService;

    private @Autowired UserService userService;

    /**
     * login by user
     * @param user
     * @return
     */
    public LoginUserResult login(User user){
        user.setTenantId(injectTenantId());
        List<User> userList = userService.login(user);
        if(userList.isEmpty()){
            throw new RTException(ResultStatus.USER_USERNAME_OR_PASSWORD_ERROR, user.getUserName());
        }
        if(userList.size() > 1){
            throw new RTException(ResultStatus.USER_USERNAME_OR_PASSWORD_ERROR, user.getUserName());
        }
        user = userList.get(0);
        if(user.getStatus() == UserStatus.FROZEN.getStatus()){
            throw new RTException(ResultStatus.USER_FROZEN_ERROR, user.getUserName());
        }
        if(user.getStatus() == UserStatus.DELETED.getStatus()){
            throw new RTException(ResultStatus.USER_DELETED_ERROR, user.getUserName());
        }
        UserGetParam getParam = new UserGetParam(user.getUserName());
        return LoginUserResult.builder()
                .token(jwtService.createToken(user))
                .user(user)
                .resources(userService.getResources(getParam))
                .tags(userService.getTags(getParam))
                .build();
    }

    /**
     * 根据token获取用户信息
     * @return
     */
    public LoginUserResult getUserInfo() {
        String token = ServletUtil.getHeader(CommonConstants.token);
        User user = jwtService.parseTokenToUser(token);
        UserGetParam getParam = new UserGetParam(user.getUserName());
        List<Tag> tags = userService.getTags(getParam);
        List<Resource> resources = userService.getResources(getParam);
        return LoginUserResult.builder()
                .token(token)
                .user(user)
                .resources(resources)
                .tags(tags)
                .build();
    }
}
