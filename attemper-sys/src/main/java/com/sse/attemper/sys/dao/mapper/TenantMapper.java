package com.sse.attemper.sys.dao.mapper;

import com.sse.attemper.common.base.BaseMapper;
import com.sse.attemper.common.result.sys.tenant.InstanceInfo;
import com.sse.attemper.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TenantMapper extends BaseMapper<Tenant> {

    Tenant get(String id);

    List<Tenant> list(Map<String, Object> paramMap);

    void delete(String[] ids);

    void save(Map<String, Object> paramMap);

    Tenant getByAdmin(String admin);

    void saveInstance(Map<String, Object> paramMap);

    List<InstanceInfo> listInstances(String id);

    void deleteInstance(InstanceInfo param);
}
