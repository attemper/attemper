package com.github.attemper.interior.dao.mapper;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.config.base.entity.ApiLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CleanLogMapper extends BaseMapper<ApiLog> {

}
