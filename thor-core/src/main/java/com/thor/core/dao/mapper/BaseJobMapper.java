package com.thor.core.dao.mapper;

import com.thor.common.base.BaseMapper;
import com.thor.sdk.common.result.job.BaseJob;
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

    @Override
    void update(BaseJob baseJob);
}
