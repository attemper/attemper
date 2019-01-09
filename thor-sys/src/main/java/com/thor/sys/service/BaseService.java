package com.thor.sys.service;

import com.stark.sdk.common.result.tenant.Tenant;
import com.stark.sdk.common.result.user.User;

public interface BaseService {

    /*    *//**
     * 在service中注入用户
     * @return
     */
    User injectUser();

    /**
     * 被管理的租户，而非超管
     * @return
     */
    Tenant injectAdminedTenant();
}
