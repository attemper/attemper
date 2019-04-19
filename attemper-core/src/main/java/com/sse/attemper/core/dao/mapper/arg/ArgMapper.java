package com.sse.attemper.core.dao.mapper.arg;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.dispatch.arg.Arg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ArgMapper extends BaseMapper<Arg> {
    @Override
    void add(Arg argument);

    @Override
    void update(Arg argument);

    List<Arg> list(Map<String, Object> paramMap);

    Arg get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);
}