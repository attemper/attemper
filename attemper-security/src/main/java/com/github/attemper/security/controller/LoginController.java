package com.github.attemper.security.controller;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.sys.login.LoginInfo;
import com.github.attemper.java.sdk.common.constant.SdkAPIPath;
import com.github.attemper.java.sdk.common.param.sys.login.LoginParam;
import com.github.attemper.java.sdk.common.result.sys.login.LoginResult;
import com.github.attemper.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Login")
@RestController
public class LoginController {

    @Autowired
    private LoginService service;

    @ApiOperation("Login by userName and password")
    @ApiImplicitParam(value = "LoginParam", name = "param", dataType = "LoginParam", required = true)
    @PostMapping(SdkAPIPath.LoginPath.$)
    public CommonResult<LoginResult> login(@RequestBody LoginParam param) {
        return CommonResult.putResult(service.login(param));
    }

    @ApiOperation("Login by userName and encoded password")
    @ApiImplicitParam(value = "LoginParam", name = "param", dataType = "LoginParam", required = true)
    @PostMapping(SdkAPIPath.LoginPath.LOGIN_BY_ENCODED_USERNAME_PWD)
    public CommonResult<LoginResult> loginByEncoded(@RequestBody LoginParam param) {
        return CommonResult.putResult(service.loginByEncoded(param));
    }

    /**
     * get by token
     *
     * @return
     */
    @ApiOperation("Get tenant&resource&tag by token")
    @GetMapping(APIPath.LoginPath.INFO)
    public CommonResult<LoginInfo> getInfo() {
        return CommonResult.putResult(service.getInfo());
    }
}
