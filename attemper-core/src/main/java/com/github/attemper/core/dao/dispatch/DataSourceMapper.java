package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.datasource.DataSourceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataSourceMapper extends BaseMapper<DataSourceInfo> {

}