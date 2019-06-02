package com.github.attemper.core.dao.mapper.arg;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.arg.Arg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ArgMapper extends BaseMapper<Arg> {

}