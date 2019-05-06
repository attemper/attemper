package com.github.attemper.sys.dao.mapper;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ldang
 */
@Mapper
@Repository
public interface TenantMapper extends BaseMapper<Tenant> {

    Tenant get(String id);

    List<Tenant> list(Map<String, Object> paramMap);

    void delete(List<String> ids);

    void save(Map<String, Object> paramMap);

    Tenant getByAdmin(String admin);
}
