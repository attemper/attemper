package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.app.project.Project;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.github.attemper.common.result.dispatch.job.Job;
import com.github.attemper.common.result.dispatch.job.JobWithVersionResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface JobMapper extends BaseMapper<Job> {

    void updateContent(Job job);

    List<JobWithVersionResult> versions(Map<String, Object> paramMap);

    void updateStatus(Map<String, Object> paramMap);

    Project getProject(Map<String, Object> paramMap);

    void saveProject(Map<String, Object> paramMap);

    List<ArgAllocatedResult> listArg(Map<String, Object> paramMap);

    List<Arg> getAllArg(Map<String, Object> paramMap);

    void addArg(Map<String, Object> paramMap);

    void deleteArg(Map<String, Object> paramMap);
}
