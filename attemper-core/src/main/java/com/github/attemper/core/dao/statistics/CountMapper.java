package com.github.attemper.core.dao.statistics;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CountMapper {

    int getTenantCount();

    List<Map<String, Object>> getJobCount(Map<String, Object> paramMap);

    List<Map<String, Object>> getInstanceCount(Map<String, Object> paramMap);
}
