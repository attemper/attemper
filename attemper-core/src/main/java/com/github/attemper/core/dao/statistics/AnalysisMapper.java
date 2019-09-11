package com.github.attemper.core.dao.statistics;

import com.github.attemper.common.result.statistics.analysis.AppPlanWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AnalysisMapper {

    List<AppPlanWrapper> getNextFireTimeWithJobName(Map<String, Object> paramMap);

}
