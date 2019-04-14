package com.sse.attemper.core.dao.mapper.job;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.dispatch.job.BaseJob;
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
public interface BaseJobMapper extends BaseMapper<BaseJob> {

    List<BaseJob> list(Map<String, Object> paramMap);

    BaseJob get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

    @Override
    void add(BaseJob baseJob);

    void addInfo(BaseJob baseJob);

    @Override
    void update(BaseJob baseJob);

    void updateInfo(BaseJob baseJob);

    List<BaseJob> versions(Map<String, Object> paramMap);

    Project getProject(Map<String, Object> paramMap);

    void saveProject(Map<String, Object> paramMap);
}
