package com.github.attemper.sys.init;

import com.github.attemper.sys.conf.SysProperties;
import com.github.attemper.sys.service.TenantService;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.tenant.TenantGetParam;
import com.github.attemper.common.result.sys.tenant.Tenant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Servlet容器启动完成后执行
 * @author ldang
 */
@Component
public class CustomPostConstruct {

    private @Autowired
    SysProperties sysProperties;

    private @Autowired
    TenantService tenantService;

    @PostConstruct
    public void loadSuperTenantFromDB() {
        String id = sysProperties.getSuperTenant().getId();
        if(StringUtils.isBlank(id)){
            throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, "The super tenant id is missing");  //必须配置超管租户的租户编号
        }
        Tenant tenant = tenantService.get(new TenantGetParam(id));
        if(tenant == null){
            throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, "Can not find the tenant in database");  //如果使用多租户，则数据库必须配置超管租户
        }
        sysProperties.setSuperTenant(tenant);
    }
}


