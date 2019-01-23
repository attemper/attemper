package com.thor.sys.service;

import com.thor.sdk.common.result.tenant.Tenant;
import com.thor.sdk.common.result.user.User;

public interface BaseService {

    /**
     * 在service中注入用户
     * @return
     */
    User injectUser();

    /**
     * 从header中取出租户
     *
     * @return
     */
    String injectTenantId();

    /**
     * 被管理的租户，而非超管
     * @return
     */
    Tenant injectAdminedTenant();

}
