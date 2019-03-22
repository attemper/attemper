package com.sse.attemper.sys.dao.mapper;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.sdk.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    Tenant get(String id);

    List<Tenant> list(Map<String, Object> paramMap);

    void delete(String[] ids);

    void save(Map<String, Object> paramMap);

    Tenant getByAdmin(String admin);
}
