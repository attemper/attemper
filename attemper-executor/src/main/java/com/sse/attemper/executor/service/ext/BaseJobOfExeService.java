package com.sse.attemper.executor.service.ext;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.core.dao.mapper.job.BaseJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BaseJobOfExeService extends BaseOfExeServiceAdapter{

    @Autowired
    private BaseJobMapper mapper;

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
