package com.sse.attemper.core.dao.mapper.job;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.sdk.common.result.dispatch.job.BaseJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
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
}
