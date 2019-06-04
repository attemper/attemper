package com.github.attemper.core.service.instance;

import com.github.attemper.common.param.dispatch.instance.JobInstanceActParam;
import com.github.attemper.common.param.dispatch.instance.JobInstanceListParam;
import com.github.attemper.common.result.dispatch.instance.JobInstance;
import com.github.attemper.common.result.dispatch.instance.JobInstanceAct;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.core.dao.mapper.instance.JobInstanceMapper;
import com.github.attemper.core.service.job.JobService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JobInstanceService extends BaseServiceAdapter {

    @Autowired
    private JobInstanceMapper mapper;

    @Autowired
    private JobService jobService;

    public JobInstance get(String id) {
        return mapper.get(id);
    }

    public JobInstanceAct getAct(String actInstId) {
        return mapper.getAct(actInstId);
    }

    public Map<String, Object> list(JobInstanceListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<JobInstance> list = (Page<JobInstance>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public List<JobInstanceAct> listAct(JobInstanceActParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.listAct(paramMap);
    }

    public void add(JobInstance jobInstance) {
        if (jobInstance.getDisplayName() == null) {
            Job job = jobService.get(jobInstance.getJobName(), jobInstance.getTenantId());
            if (job != null) {
                jobInstance.setDisplayName(job.getDisplayName());
            }
        }
        mapper.add(jobInstance);
    }

    public void update(JobInstance jobInstance) {
        mapper.update(jobInstance);
    }

    public void addAct(JobInstanceAct jobInstanceAct) {
        mapper.addAct(jobInstanceAct);
    }

    public void updateAct(JobInstanceAct jobInstanceAct) {
        mapper.updateAct(jobInstanceAct);
    }
}
