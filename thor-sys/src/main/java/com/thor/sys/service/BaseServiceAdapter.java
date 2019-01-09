package com.thor.sys.service;

import com.stark.sdk.common.result.tenant.Tenant;
import com.stark.sdk.common.result.user.User;
import com.thor.common.constant.CommonConstants;
import com.thor.sys.holder.AdminInfoHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;

import java.util.Map;

/**
 * @author ldang
 */
public abstract class BaseServiceAdapter implements BaseService {

    /**
     * 获取线程池中的用户
     * @return
     */
    @Override
    public User injectUser() {
        return AdminInfoHolder.get().getUser();
    }

    @Override
    public Tenant injectAdminedTenant() {
        return AdminInfoHolder.get().getTenant();
    }

    /**
     * 将对象转为map并设置被管理的tenantId
     * @param obj
     * @return
     */
    protected Map<String, Object> injectAdminedTenantIdToMap(Object obj) {
        Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
        paramMap.put(CommonConstants.tenantId, injectAdminedTenant().getId());
        return paramMap;
    }
}
