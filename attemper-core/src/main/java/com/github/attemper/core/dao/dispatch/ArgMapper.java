package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.result.dispatch.arg.Arg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ArgMapper {

    void add(Arg model);

    void addBatch(List<Arg> models);

    void update(Arg model);

    Arg get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

    List<Arg> list(Map<String, Object> paramMap);
}