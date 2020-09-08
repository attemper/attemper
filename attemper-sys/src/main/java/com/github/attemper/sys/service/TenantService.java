package com.github.attemper.sys.service;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.sys.tenant.*;
import com.github.attemper.common.result.sys.role.Role;
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

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TenantService extends BaseServiceAdapter {

    @Autowired
    private TenantMapper mapper;

    public Tenant get(TenantNameParam param) {
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
        Tenant tenant = get(new TenantNameParam().setUserName(param.getUserName()));
        if(tenant != null){
            throw new DuplicateKeyException(param.getUserName());
        }
        tenant = toTenant(param);
        tenant.setPassword(PasswordUtil.encode(tenant.getPassword(), tenant.getUserName()))
                .setSuperAdmin(0); // just add common tenant
        mapper.add(tenant);
        return tenant;
    }

    public Tenant update(TenantSaveParam param) {
        Tenant tenant = validateAndGet(param.getUserName());
        if (!StringUtils.equals(tenant.getPassword(), PasswordUtil.encode(param.getPassword(), param.getUserName()))) {
            throw new RTException(1301);
        }
        tenant = toTenant(param);
        mapper.update(tenant);
        return tenant;
    }

    public Void updatePassword(TenantChangePasswordParam param) {
        Tenant tenant = validateAndGet(param.getUserName());
        String oldEncodedPassword = PasswordUtil.encode(param.getOldPassword(), param.getUserName());
        if (!StringUtils.equals(oldEncodedPassword, tenant.getPassword())) {
            throw new RTException(5105);
        }
        String newEncodedPassword = PasswordUtil.encode(param.getNewPassword(), param.getUserName());
        if (StringUtils.equals(oldEncodedPassword, newEncodedPassword)) {
            throw new RTException(5106);
        }
        if (!StringUtils.equals(param.getNewPassword(), param.getDoubleNewPassword())) {
            throw new RTException(5107);
        }
        tenant.setPassword(newEncodedPassword);
        mapper.updatePassword(BeanUtil.bean2Map(tenant));
        return null;
    }

    public Tenant validateAndGet(String userName) {
        Tenant tenant = get(new TenantNameParam().setUserName(userName));
        if(tenant == null){
            throw new RTException(5150, userName);
        }
        return tenant;
    }

    public Void remove(TenantNamesParam param) {
        for (String userName : param.getUserNames()) {
            if (StringUtils.equals(userName, SysStore.getAdminTenant().getUserName())) {
                throw new RTException(5115, userName);
            }
        }
        mapper.delete(param.getUserNames());
        return null;
    }

    public List<String> getResources(TenantNameParam param) {
        return mapper.getResources(BeanUtil.bean2Map(param));
    }

    public List<Role> getRoles(TenantNameParam param) {
        return mapper.getRoles(BeanUtil.bean2Map(param));
    }

    public Void saveRoles(TenantRoleSaveParam param) {
        Map<String, Object> paramMap = BeanUtil.bean2Map(param);
        mapper.deleteRoles(paramMap);
        if (param.getRoleNames() != null && param.getRoleNames().size() > 0) {
            mapper.addRoles(paramMap);
        }
        return null;
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
