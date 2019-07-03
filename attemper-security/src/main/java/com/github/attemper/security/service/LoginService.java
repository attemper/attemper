package com.github.attemper.security.service;

import com.github.attemper.common.enums.TenantStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.result.sys.login.LoginInfo;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.java.sdk.common.param.sys.login.LoginParam;
import com.github.attemper.java.sdk.common.result.sys.login.LoginResult;
import com.github.attemper.sys.ext.service.JWTService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.sys.util.PasswordUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ldang
 */
@Service
public class LoginService extends BaseServiceAdapter {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JWTService jwtService;

    public LoginResult login(LoginParam param) {
        return loginByCondition(param, false);
    }

    public LoginResult loginByEncoded(LoginParam param) {
        return loginByCondition(param, true);
    }

    private LoginResult loginByCondition(LoginParam param, boolean pwdEncoded) {
        Tenant tenant = tenantService.get(new TenantGetParam().setUserName(param.getUserName()));
        if (tenant == null) {
            throw new RTException(1300, param.getUserName());
        } else if ((pwdEncoded && !StringUtils.equals(tenant.getPassword(), param.getPassword()))
                || (!pwdEncoded && !validatePassword(tenant.getPassword(), param.getPassword(), param.getUserName()))) {
            throw new RTException(1301, tenant.getUserName());
        }
        if (tenant.getStatus() == TenantStatus.FROZEN.getStatus()) {
            throw new RTException(1302, tenant.getUserName());
        } else if (tenant.getStatus() == TenantStatus.DELETED.getStatus()) {
            throw new RTException(1303, tenant.getUserName());
        }
        return new LoginResult().setToken(jwtService.createToken(tenant));
    }

    public LoginInfo getInfo() {
        TenantGetParam getParam = new TenantGetParam().setUserName(injectTenantId());
        Tenant tenant = tenantService.get(getParam);
        List<Tag> tags = tenantService.getTags(getParam);
        List<String> resources = tenantService.getResources(getParam);
        tenant.setPassword(null); // remove password because of security
        return LoginInfo.builder()
                .tenant(tenant)
                .resources(resources)
                .tags(tags)
                .build();
    }

    private boolean validatePassword(String dbPassword, String plainPassword, String userName) {
        String encodedPassword = PasswordUtil.encode(plainPassword, userName);
        return StringUtils.equals(dbPassword, encodedPassword);
    }
}
