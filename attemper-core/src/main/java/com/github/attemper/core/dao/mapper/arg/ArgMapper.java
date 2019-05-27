package com.github.attemper.core.dao.mapper.arg;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.arg.Arg;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface ArgMapper extends BaseMapper<Arg> {

    DataSourceInfo getDatasource(Map<String, Object> paramMap);

    int deleteDatasource(Map<String, Object> paramMap);

    void addDatasource(Map<String, Object> paramMap);
}