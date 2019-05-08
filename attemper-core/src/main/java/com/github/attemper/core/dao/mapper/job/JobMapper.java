package com.github.attemper.core.dao.mapper.job;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.github.attemper.common.result.dispatch.job.FlowJob;
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
public interface JobMapper extends BaseMapper<FlowJob> {

    void addInfo(FlowJob flowJob);

    void updateInfo(FlowJob flowJob);

    List<FlowJob> versions(Map<String, Object> paramMap);

    Project getProject(Map<String, Object> paramMap);

    void saveProject(Map<String, Object> paramMap);

    List<ArgAllocatedResult> listArg(Map<String, Object> paramMap);

    void addArg(Map<String, Object> paramMap);

    void deleteArg(Map<String, Object> paramMap);
}
