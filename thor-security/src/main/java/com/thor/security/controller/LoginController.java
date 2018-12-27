package com.thor.security.controller;

import com.thor.common.constant.APIPath;
import com.thor.common.result.CommonResult;
import com.thor.config.constant.APIConst;
import com.thor.security.param.LoginUserParam;
import com.thor.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = APIConst.Tag.LOGIN)
@RestController
@RequestMapping(APIPath.Sys.LOGIN)
public class LoginController {

    private @Autowired
    LoginService service;

    @ApiOperation(APIConst.Operation.Login.LOGIN)
    @ApiImplicitParam(value = "登录的用户", name = "loginUserParam", dataType = "LoginUserParam", required = true)
    @PostMapping
    public CommonResult login(@RequestBody LoginUserParam loginUserParam){
        return CommonResult.putResult(service.login(loginUserParam.toUser()));
    }

    @ApiOperation(APIConst.Operation.Login.GET_INFO_BY_TOKEN)
    @GetMapping("userInfo")
    public CommonResult getUserInfo() {
        return CommonResult.putResult(service.getUserInfo());
    }

}
