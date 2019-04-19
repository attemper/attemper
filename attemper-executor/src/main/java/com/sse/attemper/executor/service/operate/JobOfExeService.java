package com.sse.attemper.executor.service.operate;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.core.dao.mapper.job.JobMapper;
import com.sse.attemper.executor.service.BaseOfExeServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JobOfExeService extends BaseOfExeServiceAdapter {

    @Autowired
    private JobMapper mapper;

    /**
     * get project by jobName and tenantId
     * @param jobName
     * @param tenantId
     * @return
     */
    public Project getProject(String jobName, String tenantId) {
        return mapper.getProject(toJobNameMap(toTenantIdMap(tenantId), jobName));
    }

    private Map<String, Object> toJobNameMap(Map<String, Object> tenantIdMap, String jobName) {
        tenantIdMap.put(CommonConstants.jobName, jobName);
        return tenantIdMap;
    }
}
