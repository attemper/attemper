package com.thor.core.service;

import com.thor.common.constant.CommonConstants;
import com.thor.config.util.ServletUtil;

/**
 * @author ldang
 */
public abstract class BaseServiceAdapter implements BaseService {

    /**
     * 获取线程池中的用户
     * @return
     */
    /*public User injectUser() {
        return UserHolder.get();
    }
    */

    @Override
    public String injectTenantId() {
        return ServletUtil.getHeader(CommonConstants.tenantId);
    }

    /**
     * 将对象转为map并设置tenantId
     * @param obj
     * @return
     */
    /*protected Map<String, Object> injectTenantIdToMap(Object obj) {
        Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
        paramMap.put(CommonConstants.tenantId, injectTenantId());
        return paramMap;
    }*/
}
