package com.github.attemper.executor.service.operate;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.common.result.dispatch.project.ProjectInfo;
import com.github.attemper.core.service.project.ProjectService;
import com.github.attemper.executor.service.BaseOfExeServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectOfExeService extends BaseOfExeServiceAdapter {

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

    public List<ProjectInfo> listInfo(String projectName, String tenantId) {
        Map<String, Object> paramMap = toProjectNameMap(toTenantIdMap(tenantId), projectName);
        return projectService.listInfo(paramMap);
    }

    private Map<String, Object> toProjectNameMap(Map<String, Object> tenantIdMap, String projectName) {
        tenantIdMap.put(CommonConstants.projectName, projectName);
        return tenantIdMap;
    }

}
