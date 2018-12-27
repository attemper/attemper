package com.thor.core.dao.mapper;

import com.thor.common.base.BaseMapper;
import com.thor.core.entity.Resource;
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
