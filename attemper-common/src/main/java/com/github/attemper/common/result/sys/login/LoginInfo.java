package com.github.attemper.common.result.sys.login;

import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import lombok.*;

import java.util.List;

/**
 * @author ldang
 */
@ToString
public class LoginInfo {

    /** logged tenant */
    protected Tenant tenant;

    /** role*/
    protected List<Tag> tags;

    /** resource */
    protected List<String> resources;

    public Tenant getTenant() {
        return tenant;
    }

    public LoginInfo setTenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public LoginInfo setTags(List<Tag> tags) {
        this.tags = tags;
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
