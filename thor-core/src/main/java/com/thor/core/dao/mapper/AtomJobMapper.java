package com.thor.core.dao.mapper;

import com.thor.common.base.BaseMapper;
import com.thor.sdk.common.result.job.atom.AtomJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
public interface AtomJobMapper extends BaseMapper<AtomJob> {

    List<AtomJob> list(Map<String, Object> paramMap);

    AtomJob get(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

}
