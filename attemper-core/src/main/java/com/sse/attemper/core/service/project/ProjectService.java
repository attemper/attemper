package com.sse.attemper.core.service.project;

import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.param.CommonParam;
import com.sse.attemper.common.param.dispatch.project.*;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.common.result.dispatch.project.ProjectInfo;
import com.sse.attemper.core.dao.mapper.project.ProjectMapper;
import com.sse.attemper.sys.service.BaseServiceAdapter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author ldang
 */
@Service
@Transactional
public class ProjectService extends BaseServiceAdapter {

    @Autowired
	private ProjectMapper mapper;

    /**
     * @param commonParam
     * @return
     */
	public List<Project> getAll(CommonParam commonParam){
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(commonParam);
        return getAll(paramMap);
    }

    public List<Project> getAll(Map<String, Object> paramMap) {
        List<Project> sourceList = mapper.getAll(paramMap);
        List<Project> targetList = new ArrayList<>(sourceList.size());
        Project rootProject = findRootProject(sourceList);
        targetList.add(rootProject);
        computeTreeList(sourceList, targetList, rootProject);
        return targetList;
    }

    /**
     * @param saveParam
     * @return
     */
    public Project save(ProjectSaveParam saveParam) {
        Project project = toProject(saveParam);
        Date now = new Date();
        project.setCreateTime(now);
        project.setUpdateTime(now);
        mapper.save(injectAdminedTenantIdToMap(project));
        return project;
    }

    /**
     * @param removeParam
     * @return
     */
    public void remove(ProjectRemoveParam removeParam) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(removeParam);
        mapper.delete(paramMap);
    }

    private void computeTreeList(List<Project> sourceList, List<Project> targetList, Project cellProject) {
	    Iterator<Project> it = sourceList.iterator();
	    boolean find = false;
	    while(it.hasNext()){
	        Project current = it.next();
	        if(StringUtils.equals(current.getParentProjectName(), cellProject.getProjectName())){
                targetList.add(current);
                find = true;
                computeTreeList(sourceList, targetList, current);
            }
        }
        if(!find){
	        return;
        }
    }

    private Project findRootProject(List<Project> sourceList) {
	    List<Project> projects =
                sourceList.stream().filter(project -> project.getParentProjectName() == null).collect(Collectors.toList());
	    if(projects.size() != 1){
            throw new RTException(6570);
        }
        return projects.get(0);
    }

    private Project toProject(ProjectSaveParam saveParam) {
        return Project.builder()
                .projectName(saveParam.getProjectName())
                .parentProjectName(saveParam.getParentProjectName())
                .displayName(saveParam.getDisplayName())
                .contextPath(saveParam.getContextPath())
                .position(saveParam.getPosition())
                .build();
    }


    public Void saveInfo(ProjectInfoSaveParam param) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(param);
        mapper.saveInfo(paramMap);
        return null;
    }

    public Void removeInfo(ProjectInfoRemoveParam param) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(param);
        mapper.deleteInfo(paramMap);
        return null;
    }

    public List<ProjectInfo> listInfos(ProjectGetParam param) {
        Map<String, Object> paramMap = injectAdminedTenantIdToMap(param);
        return listInfos(paramMap);
    }

    public List<ProjectInfo> listInfos(Map<String, Object> paramMap) {
        return mapper.listInfos(paramMap);
    }
}
