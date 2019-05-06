package com.github.attemper.sys.service;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.common.result.sys.user.User;
import com.github.attemper.config.base.util.ServletUtil;
import com.github.attemper.sys.holder.TenantHolder;
import com.github.attemper.sys.holder.UserHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;

import java.util.HashMap;
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
        return ServletUtil.getHeader(CommonConstants.tenantId);
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
        paramMap.put(CommonConstants.tenantId, injectTenantId());
        return paramMap;
    }

    @Override
    public Tenant injectAdminTenant() {
        return TenantHolder.get();
    }

    /**
     * 将对象转为map并设置被管理的tenantId
     * @param obj
     * @return
     */
    protected Map<String, Object> injectAdminTenantIdToMap(Object obj) {
        Map<String, Object> paramMap;
        if (obj == null) {
            paramMap = new HashMap<>(1);
        } else {
            if (!(obj instanceof Map)) {
                paramMap = BeanUtil.beanToMap(obj);
            } else {
                paramMap = (Map<String, Object>) obj;
            }
        }
        paramMap.put(CommonConstants.tenantId, injectAdminTenant().getId());
        return paramMap;
    }
}
