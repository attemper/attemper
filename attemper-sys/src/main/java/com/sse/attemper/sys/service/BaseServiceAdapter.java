package com.sse.attemper.sys.service;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.result.sys.tenant.Tenant;
import com.sse.attemper.common.result.sys.user.User;
import com.sse.attemper.config.util.ServletUtil;
import com.sse.attemper.sys.holder.TenantHolder;
import com.sse.attemper.sys.holder.UserHolder;
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
        paramMap.put(CommonConstants.tenantId, injectAdminedTenant().getId());
        return paramMap;
    }
}
