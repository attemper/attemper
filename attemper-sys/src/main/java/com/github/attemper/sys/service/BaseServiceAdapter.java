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

    protected Map<String, Object> injectTenantIdToMap(Object obj) {
        Map<String, Object> paramMap;
        if (!(obj instanceof Map)) {
            paramMap = BeanUtil.bean2Map(obj);
        } else {
            paramMap = (Map<String, Object>) obj;
        }
        paramMap.put(CommonConstants.tenantId, injectTenantId());
        return paramMap;
    }

    protected Map<String, Object> injectTenantIdExceptAdminToMap(Object obj) {
        Map<String, Object> paramMap;
        if (!(obj instanceof Map)) {
            paramMap = BeanUtil.bean2Map(obj);
        } else {
            paramMap = (Map<String, Object>) obj;
        }
        if (Store.getAdminTenant().getUserName().equals(injectTenantId())) {
            paramMap.put(CommonConstants.tenantId, null);
        } else {
            paramMap.put(CommonConstants.tenantId, injectTenantId());
        }
        return paramMap;
    }

}
