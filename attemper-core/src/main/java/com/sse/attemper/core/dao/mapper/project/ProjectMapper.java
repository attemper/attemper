package com.sse.attemper.core.dao.mapper.project;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.dispatch.project.Project;
import com.sse.attemper.common.result.dispatch.project.ProjectInfo;
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

    List<ProjectInfo> listInfos(String id);

    void deleteInfo(Map<String, Object> paramMap);
}
