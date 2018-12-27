package com.thor.core.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thor.common.constant.CommonConstants;
import com.thor.common.enums.ResultStatus;
import com.thor.common.exception.RTException;
import com.thor.core.conf.CoreProperties;
import com.thor.core.dao.mapper.TenantMapper;
import com.thor.core.dao.repo.TenantRepository;
import com.thor.core.entity.Tenant;
import com.thor.core.ext.service.SecretService;
import com.thor.core.param.tenant.TenantGetParam;
import com.thor.core.param.tenant.TenantListParam;
import com.thor.core.param.tenant.TenantRemoveParam;
import com.thor.core.param.tenant.TenantSaveParam;
import com.thor.core.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

/**
 * @author ldang
 */
@Service
@Transactional
public class TenantService extends BaseServiceAdapter {

    private @Autowired TenantRepository repository;

    private @Autowired TenantMapper mapper;

    private @Autowired SecretService secretService;

    private @Autowired CoreProperties coreProperties;

    /**
     * 根据id查询租户
     * @param getParam
     * @return
     */
    public Tenant getTenant(TenantGetParam getParam){
        return repository.findById(getParam.getId());
    }

    /**
     * 查询列表
     * @param listParam
     * @return
     */
    public Map<String, Object> list(TenantListParam listParam) {
        Map<String, Object> paramMap = injectTenantIdToMap(listParam);
        if(!injectUser().getUserName().equals(coreProperties.getSuperTenant().getAdmin())){
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
        Tenant tenant = getTenant(new TenantGetParam(saveParam.getId()));
        if(tenant != null){
            throw new DuplicateKeyException(saveParam.getId());
        }
        tenant = saveParam.toTenant();
        tenant.setCreateTime(new Date());
        return save(tenant);
    }

    /**
     * 更新
     * @param saveParam
     * @return
     */
    public Tenant update(TenantSaveParam saveParam) {
        //主键应应在数据库中  存在
        Tenant tenant = getTenant(new TenantGetParam(saveParam.getId()));
        if(tenant == null){
            throw new RTException(ResultStatus.INTERFACE_TENANT_UPDATE_NOTEXIST_ERROR);  //5150
        }
        Tenant updatedTenant = saveParam.toTenant();
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
        return repository.save(tenant);
    }

    /**
     * 删除
     * @param removeParam
     * @return
     */
    public void remove(TenantRemoveParam removeParam) {
        repository.deleteByIds(removeParam.getIds());
    }
}
