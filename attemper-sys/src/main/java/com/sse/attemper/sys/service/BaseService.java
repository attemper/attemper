package com.sse.attemper.sys.service;

import com.sse.attemper.common.result.sys.tenant.Tenant;
import com.sse.attemper.common.result.sys.user.User;

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
    Tenant injectAdminTenant();

}
