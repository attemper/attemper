package com.thor.sys.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thor.sdk.common.exception.RTException;
import com.thor.sdk.common.param.sys.tenant.TenantGetParam;
import com.thor.sdk.common.param.sys.tenant.TenantListParam;
import com.thor.sdk.common.param.sys.tenant.TenantRemoveParam;
import com.thor.sdk.common.param.sys.tenant.TenantSaveParam;
import com.thor.sdk.common.result.sys.tenant.Tenant;
import com.thor.sys.conf.CoreProperties;
import com.thor.sys.dao.mapper.TenantMapper;
import com.thor.sys.ext.service.SecretService;
import com.thor.sys.util.PageUtil;
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
    private CoreProperties coreProperties;

    /**
     * 根据id查询租户
     * @param getParam
     * @return
     */
    public Tenant get(TenantGetParam getParam) {
        return mapper.get(getParam.getId());
    }

    public Tenant getByAdmin(String admin) {
        return mapper.getByAdmin(admin);
    }

    /**
     * 查询列表
     * @param listParam
     * @return
     */
    public Map<String, Object> list(TenantListParam listParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(listParam);
        if (!injectUser().getUserName().equals(coreProperties.getSuperTenant().getAdmin())) {
            //非超管，仅查出当前租户
            paramMap.put("matchAdmin", injectUser().getUserName());
        }
        PageHelper.startPage(listParam.getCurrentPage(), listParam.getPageSize());
        Page<Tenant> list = (Page<Tenant>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    /**
     * 新增
     * @param saveParam
     * @return
     */
    public Tenant add(TenantSaveParam saveParam) {
        //主键应在数据库中   不存在
        Tenant tenant = get(new TenantGetParam(saveParam.getId()));
        if(tenant != null){
            throw new DuplicateKeyException(saveParam.getId());
        }
        tenant = toTenant(saveParam);
        tenant.setCreateTime(new Date());
        return save(tenant);
    }

    /**
     * 更新
     * @param saveParam
     * @return
     */
    public Tenant update(TenantSaveParam saveParam) {
        //主键应在数据库中  存在
        Tenant tenant = get(new TenantGetParam(saveParam.getId()));
        if(tenant == null){
            throw new RTException(5150);  //5150
        }
        Tenant updatedTenant = toTenant(saveParam);
        updatedTenant.setCreateTime(tenant.getCreateTime());
        return save(updatedTenant);
    }

    /**
     * 保存
     * @param tenant
     */
    public Tenant save(Tenant tenant) {
        String sign = secretService.encode(tenant.getId());
        tenant.setSign(sign);
        tenant.setUpdateTime(new Date());
        mapper.save(BeanUtil.beanToMap(tenant));
        return tenant;
    }

    /**
     * 删除
     * @param removeParam
     * @return
     */
    public void remove(TenantRemoveParam removeParam) {
        for (String id : removeParam.getIds()) {
            if (StringUtils.equals(id, coreProperties.getSuperTenant().getId())) {
                throw new RTException(5115, id);
            }
        }
        mapper.delete(removeParam.getIds());
    }

    private Tenant toTenant(TenantSaveParam saveParam) {
        return Tenant.builder()
                .id(saveParam.getId())
                .name(saveParam.getName())
                .admin(saveParam.getAdmin())
                .build();
    }
}
