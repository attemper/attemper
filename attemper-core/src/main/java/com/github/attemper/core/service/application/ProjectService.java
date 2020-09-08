package com.github.attemper.core.service.application;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.app.project.*;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.app.project.ProjectInfo;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.core.dao.application.ProjectMapper;
import com.github.attemper.sys.holder.TenantHolder;
import com.github.attemper.sys.service.BaseServiceAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService extends BaseServiceAdapter {

    @Autowired
	private ProjectMapper mapper;

    public List<Project> getAll() {
        Map<String, Object> paramMap = injectTenantIdToMap(null);
        return getAll(paramMap);
    }

    private List<Project> getAll(Map<String, Object> paramMap) {
        List<Project> sourceList = mapper.getAll(paramMap);
        List<Project> targetList = new ArrayList<>(sourceList.size());
        Project rootProject = findRootProject(sourceList);
        targetList.add(rootProject);
        computeTreeList(sourceList, targetList, rootProject);
        return targetList;
    }

    public Project getRoot(String tenantId) {
        Map<String, Object> paramMap = injectTenantIdToMap(null, tenantId);
        return getRoot(paramMap);
    }

    private Project getRoot(Map<String, Object> paramMap) {
        List<Project> projects = getAll(paramMap);
        return findRootProject(projects);
    }

    private Project get(ProjectNameParam param) {
        return mapper.get(injectTenantIdToMap(param));
    }

    public Project save(ProjectSaveParam param) {
        Project project = toProject(param);
        if (get(new ProjectNameParam().setProjectName(param.getProjectName())) == null) {
            mapper.add(project);
        } else {
            project = toProject(param);
            mapper.update(project);
        }
        return project;
    }

    public Void remove(ProjectNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.delete(paramMap);
        return null;
    }

    private void computeTreeList(List<Project> sourceList, List<Project> targetList, Project cellProject) {
	    Iterator<Project> it = sourceList.iterator();
	    while(it.hasNext()){
	        Project current = it.next();
	        if(StringUtils.equals(current.getParentProjectName(), cellProject.getProjectName())){
                targetList.add(current);
                computeTreeList(sourceList, targetList, current);
            }
        }
    }

    private Project findRootProject(List<Project> sourceList) {
	    List<Project> projects =
                sourceList.stream().filter(project -> project.getParentProjectName() == null).collect(Collectors.toList());
        if (projects.size() == 0) {
            ProjectSaveParam root = new ProjectSaveParam()
                    .setDisplayName(TenantHolder.get().getDisplayName());
            root.setProjectName("root");
            Project project = save(root);
            projects.add(project);
        } else if (projects.size() > 1) {
            throw new RTException(6570, projects.size());
        }
        return projects.get(0);
    }

    private Project toProject(ProjectSaveParam param) {
        return new Project()
                .setProjectName(param.getProjectName())
                .setParentProjectName(param.getParentProjectName())
                .setDisplayName(param.getDisplayName())
                .setContextPath(param.getContextPath())
                .setBindExecutor(param.getBindExecutor())
                .setTenantId(injectTenantId());
    }

    public ProjectInfo saveInfo(ProjectInfoSaveParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        ProjectInfo projectInfo = new ProjectInfo()
                .setProjectName(param.getProjectName())
                .setUriType(param.getUriType())
                .setUri(param.getUri())
                .setTenantId(injectTenantId());
        if (mapper.getInfo(paramMap) == null) {
            mapper.addInfo(projectInfo);
        } else {
            mapper.updateInfo(projectInfo);
        }
        return projectInfo;
    }

    public Void removeInfo(ProjectInfoRemoveParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.deleteInfo(paramMap);
        return null;
    }

    public List<ProjectInfo> listInfo(ProjectNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return listInfo(paramMap);
    }

    public List<ProjectInfo> listInfo(String projectName, String tenantId) {
        Map<String, Object> paramMap = injectTenantIdToMap(new ProjectNameParam().setProjectName(projectName), tenantId);
        return listInfo(paramMap);
    }

    public List<ProjectInfo> listInfo(Map<String, Object> paramMap) {
        return mapper.listInfo(paramMap);
    }

    public List<String> listExecutor(ProjectNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.listExecutor(paramMap);
    }

    public List<String> listExecutor(String projectName, String tenantId) {
        Map<String, Object> paramMap = injectTenantIdToMap(new ProjectNameParam().setProjectName(projectName), tenantId);
        return mapper.listExecutor(paramMap);
    }

    public Void saveExecutors(ProjectExecutorSaveParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.deleteExecutors(paramMap);
        if (param.getExecutorUris() != null && !param.getExecutorUris().isEmpty()) {
            mapper.addExecutors(paramMap);
        }
        return null;
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AppProperties appProperties;

    public List<String> toExecutorUrls() {
        List<String> list = listExecutor(null);
        if (list.size() == 0) {
            List<ServiceInstance> instances = discoveryClient.getInstances(appProperties.getExecutor().getName());
            instances.forEach(item -> list.add(item.getUri().toString()));
        }
        return list;
    }
}
