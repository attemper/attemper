package com.thor.sys.dao.mapper;

import com.thor.common.base.BaseMapper;
import com.thor.sdk.common.result.resource.Resource;
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
