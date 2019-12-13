package com.github.attemper.sys.dao;

import com.github.attemper.common.result.sys.role.Role;
import com.github.attemper.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TenantMapper {

    void add(Tenant model);

    void update(Tenant model);

    List<Tenant> list(Map<String, Object> paramMap);

    Tenant get(String userName);

    void delete(List<String> userNames);

    Tenant getAdmin();

    List<String> getResources(Map<String, Object> paramMap);

    List<Role> getRoles(Map<String, Object> paramMap);

    void deleteRoles(Map<String, Object> paramMap);

    void addRoles(Map<String, Object> paramMap);

    void updatePassword(Map<String, Object> paramMap);
}
