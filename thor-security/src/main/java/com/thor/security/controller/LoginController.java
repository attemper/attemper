package com.thor.security.controller;

import com.stark.sdk.common.constant.StarkAPIConst;
import com.stark.sdk.common.constant.StarkAPIPath;
import com.stark.sdk.common.param.login.LoginParam;
import com.stark.sdk.common.result.CommonResult;
import com.stark.sdk.common.result.login.LoginResult;
import com.thor.security.service.LoginService;
import com.thor.sys.annotation.IgnoreAuthentication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = StarkAPIConst.Tag.LOGIN)
@RestController
public class LoginController {

    private @Autowired
    LoginService service;

    @ApiOperation(StarkAPIConst.Operation.Login.LOGIN)
    @ApiImplicitParam(value = "登录的用户", name = "loginParam", dataType = "LoginParam", required = true)
    @PostMapping(StarkAPIPath.Login.LOGIN_BY_USERNAME_PWD)
    @IgnoreAuthentication
    public CommonResult<LoginResult> login(@RequestBody LoginParam loginParam) {
        return service.login(loginParam);
    }

}
