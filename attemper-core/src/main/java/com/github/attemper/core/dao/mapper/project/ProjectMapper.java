package com.github.attemper.core.dao.mapper.project;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.project.Project;
import com.github.attemper.common.result.dispatch.project.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
@Repository
public interface ProjectMapper extends BaseMapper<Project> {

    List<Project> getAll(Map<String, Object> paramMap);

    void save(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

    void saveInfo(Map<String, Object> paramMap);

    List<ProjectInfo> listInfos(Map<String, Object> paramMap);

    void deleteInfo(Map<String, Object> paramMap);
}
