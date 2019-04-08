package com.sse.attemper.sys.dao.mapper;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.sys.resource.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> getAll(Map<String, Object> paramMap);

    void save(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);
}
