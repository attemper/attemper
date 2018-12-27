package com.thor.core.init;

import com.thor.common.enums.ResultStatus;
import com.thor.common.exception.RTException;
import com.thor.core.conf.CoreProperties;
import com.thor.core.entity.Tenant;
import com.thor.core.param.tenant.TenantGetParam;
import com.thor.core.service.TenantService;
import com.thor.core.conf.CoreProperties;
import com.thor.core.service.TenantService;
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
            throw new RTException(ResultStatus.INTERNAL_SERVER_ERROR);  //必须配置超管租户的租户编号
        }
        Tenant tenant = tenantService.getTenant(new TenantGetParam(id));
        if(tenant == null){
            throw new RTException(ResultStatus.INTERNAL_SERVER_ERROR);  //如果使用多租户，则数据库必须配置超管租户
        }
        coreProperties.setSuperTenant(tenant);
    }
}


