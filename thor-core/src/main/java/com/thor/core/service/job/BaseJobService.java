package com.thor.core.service.job;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thor.core.dao.mapper.job.BaseJobMapper;
import com.thor.sdk.common.param.dispatch.job.BaseJobGetParam;
import com.thor.sdk.common.param.dispatch.job.BaseJobListParam;
import com.thor.sdk.common.param.dispatch.job.BaseJobRemoveParam;
import com.thor.sdk.common.param.dispatch.job.BaseJobSaveParam;
import com.thor.sdk.common.result.dispatch.job.BaseJob;
import com.thor.sys.service.BaseServiceAdapter;
import com.thor.sys.util.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
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
        Date now = new Date();
        baseJob.setCreateTime(now);
        baseJob.setUpdateTime(now);
        baseJob.setReversion(1);
        if (saveParam.getJobContent() == null) {
            BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(baseJob.getJobName())
                    .name(baseJob.getDisplayName())
                    .startEvent()
                    .id("StartEvent_1")
                    .done();
            baseJob.setJobContent(Bpmn.convertToString(modelInstance));
        } else {
            baseJob.setJobContent(saveParam.getJobContent());
        }
        mapper.add(baseJob);
        mapper.addHistory(baseJob);
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
        if (saveParam.getJobContent() != null) {
            updatedJob.setJobContent(saveParam.getJobContent());
        }
        updatedJob.setCreateTime(baseJob.getCreateTime());
        updatedJob.setUpdateTime(new Date());
        if (checkNeedUpgradeReversion(baseJob, updatedJob)) { //need upgrade
            updatedJob.setReversion(baseJob.getReversion() + 1);
            mapper.addHistory(updatedJob);
        } else {
            mapper.update(updatedJob);
        }
        return updatedJob;
    }

    /**
     * 判断是否需要更新模型版本
     *
     * @param baseJob
     * @param updatedJob
     * @return
     */
    private boolean checkNeedUpgradeReversion(BaseJob baseJob, BaseJob updatedJob) {
        return !StringUtils.equals(baseJob.getDisplayName(), updatedJob.getDisplayName())
                || !StringUtils.equals(baseJob.getJobContent(), updatedJob.getJobContent());
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
                .jobContent(saveParam.getJobContent())
                .status(saveParam.getStatus())
                .remark(saveParam.getRemark())
                .tenantId(injectAdminedTenant().getId())
                .build();
    }
}
