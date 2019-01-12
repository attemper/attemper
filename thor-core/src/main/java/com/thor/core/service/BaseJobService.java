package com.thor.core.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thor.core.dao.mapper.BaseJobMapper;
import com.thor.core.util.PageUtil;
import com.thor.sdk.common.param.job.BaseJobGetParam;
import com.thor.sdk.common.param.job.BaseJobListParam;
import com.thor.sdk.common.param.job.BaseJobRemoveParam;
import com.thor.sdk.common.param.job.BaseJobSaveParam;
import com.thor.sdk.common.result.job.BaseJob;
import com.thor.sys.service.BaseServiceAdapter;
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
public class BaseJobService extends BaseServiceAdapter {

    @Autowired
    private BaseJobMapper mapper;

    /**
     * 根据id查询租户
     * @param getParam
     * @return
     */
    public BaseJob get(BaseJobGetParam getParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(getParam);
        return mapper.get(paramMap);
    }

    /**
     * 查询列表
     * @param listParam
     * @return
     */
    public Map<String, Object> list(BaseJobListParam listParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(listParam);
        PageHelper.startPage(listParam.getCurrentPage(), listParam.getPageSize());
        Page<BaseJob> list = (Page<BaseJob>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    /**
     * 新增
     * @param saveParam
     * @return
     */
    public BaseJob add(BaseJobSaveParam saveParam) {
        BaseJob baseJob = get(new BaseJobGetParam().setJobName(saveParam.getJobName()));
        if (baseJob != null) {
            throw new DuplicateKeyException(saveParam.getJobName());
        }
        baseJob = toBaseJob(saveParam);
        baseJob.setTenantId(injectAdminedTenant().getId());
        Date now = new Date();
        baseJob.setCreateTime(now);
        baseJob.setUpdateTime(now);
        mapper.add(baseJob);
        return baseJob;
    }

    /**
     * 更新
     * @param saveParam
     * @return
     */
    public BaseJob update(BaseJobSaveParam saveParam) {
        BaseJob baseJob = get(new BaseJobGetParam().setJobName(saveParam.getJobName()));
        if (baseJob == null) {
            return add(saveParam); //相当于复制后修改
        }
        BaseJob updatedJob = toBaseJob(saveParam);
        updatedJob.setCreateTime(baseJob.getCreateTime());
        updatedJob.setUpdateTime(new Date());
        mapper.update(updatedJob);
        return updatedJob;
    }

    /**
     * 删除
     * @param removeParam
     * @return
     */
    public void remove(BaseJobRemoveParam removeParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(removeParam);
        mapper.delete(paramMap);
    }

    private BaseJob toBaseJob(BaseJobSaveParam saveParam) {
        return BaseJob.builder()
                .jobName(saveParam.getJobName())
                .displayName(saveParam.getDisplayName())
                .jobType(saveParam.getJobType())
                .status(saveParam.getStatus())
                .categoryName(saveParam.getCategoryName())
                .jobContent(saveParam.getJobContent())
                .remark(saveParam.getRemark())
                .build();
    }
}
