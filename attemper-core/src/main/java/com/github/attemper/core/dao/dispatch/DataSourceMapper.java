package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DataSourceMapper {

    void add(DataSourceInfo model);

    void update(DataSourceInfo model);

    DataSourceInfo get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

    List<DataSourceInfo> list(Map<String, Object> paramMap);
}