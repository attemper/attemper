package com.sse.attemper.executor.service.ext;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.common.result.dispatch.project.ProjectInfo;
import com.sse.attemper.core.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectOfExeService extends BaseOfExeServiceAdapter{

    @Autowired
    private ProjectService projectService;

    /**
     * get all projects by tenantId
     * @param tenantId
     * @return
     */
    public List<Project> getAll(String tenantId){
        Map<String, Object> paramMap = toTenantIdMap(tenantId);
        return projectService.getAll(paramMap);
    }

    public List<ProjectInfo> listInfos(String projectName, String tenantId) {
        Map<String, Object> paramMap = toProjectNameMap(toTenantIdMap(tenantId), projectName);
        return projectService.listInfos(paramMap);
    }

    private Map<String, Object> toProjectNameMap(Map<String, Object> tenantIdMap, String projectName) {
        tenantIdMap.put(CommonConstants.projectName, projectName);
        return tenantIdMap;
    }

}
