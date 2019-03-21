package com.thor.sys.service;

import com.thor.config.util.ServletUtil;
import com.thor.sdk.common.constant.ThorSdkCommonConstants;
import com.thor.sdk.common.result.sys.tenant.Tenant;
import com.thor.sdk.common.result.sys.user.User;
import com.thor.sys.holder.TenantHolder;
import com.thor.sys.holder.UserHolder;
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
        return UserHolder.get();
    }

    @Override
    public String injectTenantId() {
        return ServletUtil.getHeader(ThorSdkCommonConstants.tenantId);
    }

    /**
     * 将对象转为map并设置tenantId
     *
     * @param obj
     * @return
     */
    protected Map<String, Object> injectTenantIdToMap(Object obj) {
        Map<String, Object> paramMap;
        if (!(obj instanceof Map)) {
            paramMap = BeanUtil.beanToMap(obj);
        } else {
            paramMap = (Map<String, Object>) obj;
        }
        paramMap.put(ThorSdkCommonConstants.tenantId, injectTenantId());
        return paramMap;
    }

    @Override
    public Tenant injectAdminedTenant() {
        return TenantHolder.get();
    }

    /**
     * 将对象转为map并设置被管理的tenantId
     * @param obj
     * @return
     */
    protected Map<String, Object> injectAdminedTenantIdToMap(Object obj) {
        Map<String, Object> paramMap;
        if (!(obj instanceof Map)) {
            paramMap = BeanUtil.beanToMap(obj);
        } else {
            paramMap = (Map<String, Object>) obj;
        }
        paramMap.put(ThorSdkCommonConstants.tenantId, injectAdminedTenant().getId());
        return paramMap;
    }
}
