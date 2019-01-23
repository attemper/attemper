package com.thor.security.service;

import com.thor.sdk.common.enums.UserStatus;
import com.thor.sdk.common.exception.RTException;
import com.thor.sdk.common.param.login.LoginParam;
import com.thor.sdk.common.result.login.LoginResult;
import com.thor.sdk.common.result.user.User;
import com.thor.security.ext.service.JWTService;
import com.thor.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * service of login
 * @author ldang
 */
@Service
public class LoginService extends BaseSecurityService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    /**
     * login by user
     * @param loginParam
     * @return
     */
    public LoginResult login(LoginParam loginParam) {
        User user = toUser(loginParam);
        user.setTenantId(injectTenantId());
        List<User> userList = userService.login(user);
        if (userList.isEmpty()) {
            throw new RTException(1300, user.getUserName());
        }
        if (userList.size() > 1) {
            throw new RTException(1300, user.getUserName());
        }
        user = userList.get(0);
        if (user.getStatus() == UserStatus.FROZEN.getStatus()) {
            throw new RTException(1302, user.getUserName());
        }
        if (user.getStatus() == UserStatus.DELETED.getStatus()) {
            throw new RTException(1303, user.getUserName());
        }
        return LoginResult.builder()
                .token(jwtService.createToken(user))
                .user(user)
                .build();
    }

    private User toUser(LoginParam loginParam) {
        return User.builder()
                .userName(loginParam.getUserName())
                .password(loginParam.getPassword())
                .build();
    }
}
