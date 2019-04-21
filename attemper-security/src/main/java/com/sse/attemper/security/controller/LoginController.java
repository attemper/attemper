package com.sse.attemper.security.controller;

import com.sse.attemper.common.constant.APIPath;
import com.sse.attemper.common.param.sys.login.LoginParam;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.common.result.sys.login.LoginResult;
import com.sse.attemper.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Login")
@RestController
public class LoginController {

    private @Autowired
    LoginService service;

    @ApiOperation("Login by userName and password")
    @ApiImplicitParam(value = "LoginParam", name = "param", dataType = "LoginParam", required = true)
    @PostMapping(APIPath.LoginPath.LOGIN_BY_USERNAME_PWD)
    public CommonResult<LoginResult> login(@RequestBody LoginParam param) {
        return CommonResult.putResult(service.login(param));
    }

}
