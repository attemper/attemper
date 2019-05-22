package com.github.attemper.core.dao.mapper.job;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.project.Project;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
@Repository
public interface JobMapper extends BaseMapper<Job> {

    void addInfo(Job job);

    void updateInfo(Job job);

    List<Job> versions(Map<String, Object> paramMap);

    Project getProject(Map<String, Object> paramMap);

    void saveProject(Map<String, Object> paramMap);

    List<ArgAllocatedResult> listArg(Map<String, Object> paramMap);

    void addArg(Map<String, Object> paramMap);

    void deleteArg(Map<String, Object> paramMap);
}
