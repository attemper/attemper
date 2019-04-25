package com.sse.attemper.core.dao.mapper.job;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.dispatch.job.ArgAllocatedResult;
import com.sse.attemper.common.result.dispatch.job.FlowJob;
import com.sse.attemper.common.result.dispatch.project.Project;
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

    List<FlowJob> list(Map<String, Object> paramMap);

    FlowJob get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

    @Override
    void add(FlowJob flowJob);

    void addInfo(FlowJob flowJob);

    @Override
    void update(FlowJob flowJob);

    void updateInfo(FlowJob flowJob);

    List<FlowJob> versions(Map<String, Object> paramMap);

    Project getProject(Map<String, Object> paramMap);

    void saveProject(Map<String, Object> paramMap);

    List<ArgAllocatedResult> listArg(Map<String, Object> paramMap);

    void addArg(Map<String, Object> paramMap);

    void deleteArg(Map<String, Object> paramMap);
}
