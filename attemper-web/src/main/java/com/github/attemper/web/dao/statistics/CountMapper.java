package com.github.attemper.web.dao.statistics;

import com.github.attemper.common.result.MapResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CountMapper {

    List<MapResult<String, Integer>> getTenantCount();

    List<MapResult<String, Integer>> getJobCount(Map<String, Object> paramMap);

    List<MapResult<String, Integer>> getInstanceCount(Map<String, Object> paramMap);
}
