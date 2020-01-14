package com.github.attemper.sys.dao;

import com.github.attemper.common.param.sys.role.RoleNameParam;
import com.github.attemper.common.param.sys.role.RoleListParam;
import com.github.attemper.common.param.sys.role.RoleResourceSaveParam;
import com.github.attemper.common.param.sys.role.RoleTenantSaveParam;
import com.github.attemper.common.result.sys.role.Role;
import com.github.attemper.common.result.sys.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {

    void add(Role model);

    void update(Role model);

    Role get(RoleNameParam param);

    void delete(List<String> roleNames);

    List<Role> list(RoleListParam param);

    List<Tenant> getTenants(RoleNameParam param);

    void deleteTenants(RoleTenantSaveParam param);

    void addTenants(RoleTenantSaveParam param);

    List<String> getResources(RoleNameParam param);

    void deleteResources(RoleResourceSaveParam param);

    void addResources(RoleResourceSaveParam param);
}
