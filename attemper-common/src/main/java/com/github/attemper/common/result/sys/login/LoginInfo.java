package com.github.attemper.common.result.sys.login;

import com.github.attemper.common.result.sys.role.Role;
import com.github.attemper.common.result.sys.tenant.Tenant;
import lombok.*;

import java.util.List;

@ToString
public class LoginInfo {

    /** logged tenant */
    protected Tenant tenant;

    /** role*/
    protected List<Role> roles;

    /** resource */
    protected List<String> resources;

    public Tenant getTenant() {
        return tenant;
    }

    public LoginInfo setTenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public LoginInfo setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public List<String> getResources() {
        return resources;
    }

    public LoginInfo setResources(List<String> resources) {
        this.resources = resources;
        return this;
    }
}
