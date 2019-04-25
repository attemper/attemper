package com.sse.attemper.core.service.job;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sse.attemper.common.param.dispatch.job.JobArgListParam;
import com.sse.attemper.common.param.dispatch.job.JobGetParam;
import com.sse.attemper.common.param.dispatch.job.JobListParam;
import com.sse.attemper.common.param.dispatch.job.JobProjectSaveParam;
import com.sse.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.sse.attemper.common.result.dispatch.job.FlowJob;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.core.dao.mapper.job.JobMapper;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import com.sse.attemper.sys.util.PageUtil;
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
    public FlowJob get(JobGetParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public Map<String, Object> list(JobListParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<FlowJob> list = (Page<FlowJob>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    /**
     * list all versions by a specified job name
     *
     * @param param
     * @return
     */
    public List<FlowJob> versions(JobGetParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        return mapper.versions(paramMap);
    }

    public Project getProject(JobGetParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        return mapper.getProject(paramMap);
    }

    public Void saveProject(JobProjectSaveParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        mapper.saveProject(paramMap);
        return null;
    }

    public Map<String, Object> listArg(JobArgListParam param) {
        Map<String, Object> paramMap = injectAdminTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<ArgAllocatedResult> list = (Page<ArgAllocatedResult>) mapper.listArg(paramMap);
        return PageUtil.toResultMap(list);
    }
}
