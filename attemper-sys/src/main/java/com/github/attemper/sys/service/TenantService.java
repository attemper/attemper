package com.github.attemper.sys.service;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.tenant.*;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.sys.dao.TenantMapper;
import com.github.attemper.sys.store.SysStore;
import com.github.attemper.sys.util.PageUtil;
import com.github.attemper.sys.util.PasswordUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Service
@Transactional
public class TenantService extends BaseServiceAdapter {

    @Autowired
    private TenantMapper mapper;

    public Tenant get(TenantGetParam param) {
        return mapper.get(param.getUserName());
    }

    /**
     * get super admin
     *
     * @return
     */
    public Tenant getAdmin() {
        return mapper.getAdmin();
    }

    public Map<String, Object> list(TenantListParam param) {
        Map<String, Object> paramMap = injectTenantIdExceptAdminToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Tenant> list = (Page<Tenant>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public Tenant add(TenantSaveParam param) {
        Tenant tenant = get(new TenantGetParam().setUserName(param.getUserName()));
        if(tenant != null){
            throw new DuplicateKeyException(param.getUserName());
        }
        tenant = toTenant(param);
        tenant.setCreateTime(new Date());
        return save(tenant);
    }

    public Tenant update(TenantSaveParam param) {
        Tenant tenant = get(new TenantGetParam().setUserName(param.getUserName()));
        if(tenant == null){
            throw new RTException(5150);
        }
        Tenant updatedTenant = toTenant(param);
        updatedTenant.setCreateTime(tenant.getCreateTime());
        return save(updatedTenant);
    }

    public Tenant save(Tenant tenant) {
        tenant.setPassword(PasswordUtil.encode(tenant.getPassword(), tenant.getUserName()));
        tenant.setUpdateTime(new Date());
        mapper.save(BeanUtil.bean2Map(tenant));
        return tenant;
    }

    public Void remove(TenantRemoveParam param) {
        for (String userName : param.getUserNames()) {
            if (StringUtils.equals(userName, SysStore.getAdminTenant().getUserName())) {
                throw new RTException(5115, userName);
            }
        }
        mapper.delete(param.getUserNames());
        return null;
    }

    public List<String> getResources(TenantGetParam param) {
        return mapper.getResources(BeanUtil.bean2Map(param));
    }

    public List<Tag> getTags(TenantGetParam param) {
        return mapper.getTags(BeanUtil.bean2Map(param));
    }

    public void saveTags(TenantTagSaveParam param) {
        Map<String, Object> paramMap = BeanUtil.bean2Map(param);
        mapper.deleteTags(paramMap);
        if (param.getTagNames() == null || param.getTagNames().isEmpty()) {
            return;
        }
        mapper.addTags(paramMap);
    }

    private Tenant toTenant(TenantSaveParam param) {
        return new Tenant()
                .setUserName(param.getUserName())
                .setDisplayName(param.getDisplayName())
                .setPassword(param.getPassword())
                .setEmail(param.getEmail())
                .setMobile(param.getMobile())
                .setStatus(param.getStatus())
                .setSendConfig(param.getSendConfig());
    }

}
