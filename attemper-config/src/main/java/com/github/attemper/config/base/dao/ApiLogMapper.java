package com.github.attemper.config.base.dao;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.config.base.entity.ApiLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ApiLogMapper extends BaseMapper<ApiLog> {

}
