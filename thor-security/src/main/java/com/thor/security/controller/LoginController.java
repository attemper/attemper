package com.thor.security.controller;

import com.stark.sdk.common.constant.StarkAPIPath;
import com.stark.sdk.common.param.login.LoginParam;
import com.stark.sdk.common.result.CommonResult;
import com.thor.config.constant.APIConst;
import com.thor.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = APIConst.Tag.LOGIN)
@RestController
public class LoginController {

    private @Autowired
    LoginService service;

    @ApiOperation(APIConst.Operation.Login.LOGIN)
    @ApiImplicitParam(value = "登录的用户", name = "loginParam", dataType = "LoginParam", required = true)
    @PostMapping(StarkAPIPath.Login.LOGIN_BY_USERNAME_PWD)
    public CommonResult login(@RequestBody LoginParam loginParam) {
        return service.login(loginParam);
    }

}
