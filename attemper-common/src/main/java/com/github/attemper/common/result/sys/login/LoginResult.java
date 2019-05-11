package com.github.attemper.common.result.sys.login;

import com.github.attemper.common.result.sys.tenant.Tenant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult{

    /** access token based on jwt*/
    protected String token;

    /** 登录用户的信息 */
    protected Tenant tenant;

}
