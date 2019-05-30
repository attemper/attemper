package com.github.attemper.sys.service;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.sys.holder.TenantHolder;
import com.github.attemper.sys.store.Store;

import java.util.Map;

/**
 * @author ldang
 */
public abstract class BaseServiceAdapter implements BaseService {

    @Override
    public String injectTenantId() {
        return TenantHolder.get().getUserName();
    }

    protected Map<String, Object> injectTenantIdToMap(Object obj, String tenantId) {
        Map<String, Object> paramMap = obj2Map(obj);
        paramMap.put(CommonConstants.tenantId, tenantId == null ? injectTenantId() : tenantId);
        return paramMap;
    }

    protected Map<String, Object> injectTenantIdToMap(Object obj) {
        return injectTenantIdToMap(obj, null);
    }

    protected Map<String, Object> injectTenantIdExceptAdminToMap(Object obj) {
        Map<String, Object> paramMap = obj2Map(obj);
        if (Store.getAdminTenant().getUserName().equals(injectTenantId())) {
            paramMap.put(CommonConstants.tenantId, null);
        } else {
            paramMap.put(CommonConstants.tenantId, injectTenantId());
        }
        return paramMap;
    }

    private Map<String, Object> obj2Map(Object obj) {
        if (!(obj instanceof Map)) {
            return BeanUtil.bean2Map(obj);
        } else {
            return (Map<String, Object>) obj;
        }
    }
}
