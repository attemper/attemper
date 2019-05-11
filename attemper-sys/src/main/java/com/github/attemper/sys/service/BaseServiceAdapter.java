package com.github.attemper.sys.service;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.sys.holder.TenantHolder;

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
}
