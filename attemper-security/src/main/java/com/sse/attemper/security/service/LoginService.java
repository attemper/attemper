package com.sse.attemper.security.service;

import com.sse.attemper.common.enums.UserStatus;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.sys.login.LoginParam;
import com.sse.attemper.common.result.sys.login.LoginResult;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.security.ext.service.JWTService;
import com.sse.attemper.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ldang
 */
@Service
public class LoginService extends BaseSecurityService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    public LoginResult login(LoginParam param) {
        User user = toUser(param);
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
