package com.thor.core.dao.mapper.arg;

import com.thor.common.base.BaseMapper;
import com.thor.sdk.common.result.dispatch.arg.Arg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArgMapper extends BaseMapper<Arg> {
    @Override
    void add(Arg argument);

    @Override
    void update(Arg argument);

    List<Arg> list(Map<String, Object> paramMap);

    Arg get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);
}