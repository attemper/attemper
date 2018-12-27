package com.thor.core.dao.mapper;


import com.thor.core.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TenantMapper {

    List<Tenant> list(Map<String, Object> paramMap);
}
