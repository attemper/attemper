package com.github.attemper.core.dao.mapper.arg;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.arg.Arg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ArgMapper extends BaseMapper<Arg> {

    @Override
    void add(Arg arg);

    @Override
    void update(Arg arg);

    List<Arg> list(Map<String, Object> paramMap);

    Arg get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);
}