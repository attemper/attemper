package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.result.dispatch.delay.DelayJob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DelayJobMapper {

    void add(DelayJob model);

    DelayJob get(String id);

    List<DelayJob> list(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);
}
