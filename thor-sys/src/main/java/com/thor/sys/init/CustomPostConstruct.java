package com.thor.sys.init;

import com.thor.sdk.common.constant.ThorSdkCommonConstants;
import com.thor.sdk.common.exception.RTException;
import com.thor.sdk.common.param.tenant.TenantGetParam;
import com.thor.sdk.common.result.tenant.Tenant;
import com.thor.sys.conf.CoreProperties;
import com.thor.sys.service.TenantService;
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
    CoreProperties coreProperties;

    private @Autowired
    TenantService tenantService;

    @PostConstruct
    public void loadSuperTenantFromDB() {
        String id = coreProperties.getSuperTenant().getId();
        if(StringUtils.isBlank(id)){
            throw new RTException(ThorSdkCommonConstants.INTERNAL_SERVER_ERROR, "The super tenant id is missing");  //必须配置超管租户的租户编号
        }
        Tenant tenant = tenantService.get(new TenantGetParam(id));
        if(tenant == null){
            throw new RTException(ThorSdkCommonConstants.INTERNAL_SERVER_ERROR, "Can not find the tenant in database");  //如果使用多租户，则数据库必须配置超管租户
        }
        coreProperties.setSuperTenant(tenant);
    }
}


