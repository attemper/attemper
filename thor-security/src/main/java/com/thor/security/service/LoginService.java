package com.thor.security.service;

import com.stark.sdk.common.param.login.LoginParam;
import com.stark.sdk.common.result.CommonResult;
import com.stark.sdk.common.result.login.LoginResult;
import com.stark.sdk.microservice.client.StarkClient;
import com.thor.sys.service.BaseServiceAdapter;
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
}
