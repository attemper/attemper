package com.github.attemper.sys.dao.mapper;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ldang
 */
@Mapper
@Repository
public interface TenantMapper extends BaseMapper<Tenant> {

    Tenant get(String id);

    void delete(List<String> ids);

    Tenant getByAdmin(String admin);
}
