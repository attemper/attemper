package com.sse.attemper.sys.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.sys.tenant.TenantGetParam;
import com.sse.attemper.common.param.sys.tenant.TenantListParam;
import com.sse.attemper.common.param.sys.tenant.TenantRemoveParam;
import com.sse.attemper.common.param.sys.tenant.TenantSaveParam;
import com.sse.attemper.common.result.sys.tenant.Tenant;
import com.sse.attemper.sys.conf.SysProperties;
import com.sse.attemper.sys.dao.mapper.TenantMapper;
import com.sse.attemper.sys.ext.service.SecretService;
import com.sse.attemper.sys.util.PageUtil;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author ldang
 */
@Service
@Transactional
public class TenantService extends BaseServiceAdapter {

    @Autowired
    private TenantMapper mapper;

    @Autowired
    private SecretService secretService;

    @Autowired
    private SysProperties sysProperties;

    public Tenant get(TenantGetParam param) {
        return mapper.get(param.getId());
    }

    public Tenant getByAdmin(String admin) {
        return mapper.getByAdmin(admin);
    }

    public Map<String, Object> list(TenantListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        if (!injectUser().getUserName().equals(sysProperties.getSuperTenant().getAdmin())) {
            paramMap.put("matchAdmin", injectUser().getUserName());  //only find current tenant of the user
        }
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Tenant> list = (Page<Tenant>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public Tenant add(TenantSaveParam param) {
        Tenant tenant = get(new TenantGetParam(param.getId()));
        if(tenant != null){
            throw new DuplicateKeyException(param.getId());
        }
        tenant = toTenant(param);
        tenant.setCreateTime(new Date());
        return save(tenant);
    }

    public Tenant update(TenantSaveParam param) {
        Tenant tenant = get(new TenantGetParam(param.getId()));
        if(tenant == null){
            throw new RTException(5150);
        }
        Tenant updatedTenant = toTenant(param);
        updatedTenant.setCreateTime(tenant.getCreateTime());
        return save(updatedTenant);
    }

    public Tenant save(Tenant tenant) {
        String sign = secretService.encode(tenant.getId());
        tenant.setSign(sign);
        tenant.setUpdateTime(new Date());
        mapper.save(BeanUtil.beanToMap(tenant));
        return tenant;
    }

    public Void remove(TenantRemoveParam param) {
        for (String id : param.getIds()) {
            if (StringUtils.equals(id, sysProperties.getSuperTenant().getId())) {
                throw new RTException(5115, id);
            }
        }
        mapper.delete(param.getIds());
        return null;
    }

    private Tenant toTenant(TenantSaveParam param) {
        return Tenant.builder()
                .id(param.getId())
                .name(param.getName())
                .admin(param.getAdmin())
                .build();
    }

}
