package com.sse.attemper.sys.dao.mapper;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.sdk.common.result.sys.resource.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> getAll(Map<String, Object> paramMap);

    void save(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);
}
