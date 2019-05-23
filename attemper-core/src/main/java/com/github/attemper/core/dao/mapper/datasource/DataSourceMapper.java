package com.github.attemper.core.dao.mapper.datasource;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataSourceMapper extends BaseMapper<DataSourceInfo> {

    List<DataSourceInfo> getByNames(Map<String, Object> paramMap);
}