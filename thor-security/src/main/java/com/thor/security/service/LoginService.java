package com.thor.security.service;

import com.stark.sdk.common.param.login.LoginParam;
import com.stark.sdk.common.result.CommonResult;
import com.stark.sdk.common.result.login.LoginResult;
import com.stark.sdk.microservice.client.StarkClient;
import com.thor.core.service.BaseServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service of login
 * @author ldang
 */
@Service
public class LoginService extends BaseServiceAdapter {

    /**
     * @see com.thor.config.conf.StarkClientConfig
     */
    @Autowired
    private StarkClient starkClient;

    /**
     * login by user
     * @param loginParam
     * @return
     */
    public CommonResult<LoginResult> login(LoginParam loginParam) {
        return starkClient.login(loginParam);
    }

    /*private User toUser(LoginParam loginParam) {
        return User.builder()
                .userName(loginParam.getUserName())
                .password(loginParam.getPassword())
                .build();
    }*/

    /**
     * 根据token获取用户信息
     * @return
     */
    /*public LoginResult getUserInfo() {
        String token = ServletUtil.getHeader(StarkSdkCommonConstants.token);
        User user = jwtService.parseTokenToUser(token);
        UserGetParam getParam = new UserGetParam(user.getUserName());
        List<Tag> tags = userService.getTags(getParam);
        List<Resource> resources = userService.getResources(getParam);
        return LoginResult.builder()
                .token(token)
                .user(user)
                .resources(resources)
                .tags(tags)
                .build();
    }*/
}
