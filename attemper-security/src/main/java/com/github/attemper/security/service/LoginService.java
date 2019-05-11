package com.github.attemper.security.service;

import com.github.attemper.common.enums.TenantStatus;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.login.LoginParam;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.result.sys.login.LoginInfo;
import com.github.attemper.common.result.sys.login.LoginResult;
import com.github.attemper.common.result.sys.resource.Resource;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.sys.ext.service.JWTService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.service.TenantService;
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
    private JWTService jwtService;

    @Autowired
    private TenantService tenantService;

    public LoginResult login(LoginParam param) {
        Tenant tenant = tenantService.get(new TenantGetParam(param.getUserName()));
        if (tenant == null) {
            throw new RTException(1300, tenant.getUserName());
        } else if (!StringUtils.equals(tenant.getPassword(), param.getPassword())) {
            throw new RTException(1301, tenant.getUserName());
        }
        if (tenant.getStatus() == TenantStatus.FROZEN.getStatus()) {
            throw new RTException(1302, tenant.getUserName());
        } else if (tenant.getStatus() == TenantStatus.DELETED.getStatus()) {
            throw new RTException(1303, tenant.getUserName());
        }
        tenant.setPassword(null);
        return LoginResult.builder()
                .token(jwtService.createToken(tenant))
                .tenant(tenant)
                .build();
    }

    public LoginInfo getInfo() {
        TenantGetParam getParam = new TenantGetParam(injectTenantId());
        Tenant tenant = tenantService.get(getParam);
        List<Tag> tags = tenantService.getTags(getParam);
        List<Resource> resources = tenantService.getResources(getParam);
        return LoginInfo.builder()
                .tenant(tenant)
                .resources(resources)
                .tags(tags)
                .build();
    }
}
