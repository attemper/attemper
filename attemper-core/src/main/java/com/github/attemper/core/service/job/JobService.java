package com.github.attemper.core.service.job;

import com.github.attemper.common.param.dispatch.job.JobArgListParam;
import com.github.attemper.common.param.dispatch.job.JobGetParam;
import com.github.attemper.common.param.dispatch.job.JobProjectSaveParam;
import com.github.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.core.dao.mapper.job.JobMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Service
@Transactional
public class JobService extends BaseServiceAdapter {

    @Autowired
    private JobMapper mapper;

    /**
     * get job by jobName
     * @param param
     * @return
     */
    public Job get(JobGetParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    /**
     * list all versions by a specified job displayName
     *
     * @param param
     * @return
     */
    public List<Job> versions(JobGetParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.versions(paramMap);
    }

    public Project getProject(JobGetParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.getProject(paramMap);
    }

    public Void saveProject(JobProjectSaveParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.saveProject(paramMap);
        return null;
    }

    public Map<String, Object> listArg(JobArgListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<ArgAllocatedResult> list = (Page<ArgAllocatedResult>) mapper.listArg(paramMap);
        return PageUtil.toResultMap(list);
    }
}
