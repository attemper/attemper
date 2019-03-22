package com.sse.attemper.security.controller;

import com.sse.attemper.sdk.common.constant.APIConst;
import com.sse.attemper.sdk.common.constant.APIPath;
import com.sse.attemper.sdk.common.param.sys.login.LoginParam;
import com.sse.attemper.sdk.common.result.CommonResult;
import com.sse.attemper.sdk.common.result.sys.login.LoginResult;
import com.sse.attemper.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = APIConst.APITag.LOGIN)
@RestController
public class LoginController {

    private @Autowired
    LoginService service;

    @ApiOperation(APIConst.APIOperation.LoginTitle.LOGIN)
    @ApiImplicitParam(value = "登录的用户", name = "loginParam", dataType = "LoginParam", required = true)
    @PostMapping(APIPath.LoginPath.LOGIN_BY_USERNAME_PWD)
    public CommonResult<LoginResult> login(@RequestBody LoginParam loginParam) {
        return CommonResult.putResult(service.login(loginParam));
    }

}
