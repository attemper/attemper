package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.job.JobWithVersionResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JobMapper {

    void add(Job model);

    void update(Job model);

    Job get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

    List<Job> list(Map<String, Object> paramMap);

    void updateContent(Job job);

    List<JobWithVersionResult> versions(Map<String, Object> paramMap);

    void updateStatus(Map<String, Object> paramMap);

    Project getProject(Map<String, Object> paramMap);

    void deleteProject(Map<String, Object> paramMap);

    void addProject(Map<String, Object> paramMap);

    List<ArgAllocatedResult> listArg(Map<String, Object> paramMap);

    List<Arg> getArg(Map<String, Object> paramMap);

    void addArg(Map<String, Object> paramMap);

    void deleteArg(Map<String, Object> paramMap);

    void addJobArg(Map<String, Object> paramMap);

    void deleteJobArg(Map<String, Object> paramMap);

    void addConditions(List<Map<String, Object>> conditions);

    void addJobCondition(Map<String, Object> paramMap);

    void addJobConditions(List<Map<String, Object>> conditions);

    void deleteJobCondition(Map<String, Object> paramMap);

    void deleteCondition(Map<String, Object> paramMap);

    List<Condition> getConditions(Map<String, Object> paramMap);
}
