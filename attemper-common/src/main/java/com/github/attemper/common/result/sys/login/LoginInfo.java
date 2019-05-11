package com.github.attemper.common.result.sys.login;

import com.github.attemper.common.result.sys.resource.Resource;
import com.github.attemper.common.result.sys.tag.Tag;
import com.github.attemper.common.result.sys.tenant.Tenant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {

    /** logged tenant */
    protected Tenant tenant;

    /** role*/
    protected List<Tag> tags;

    /** resource */
    protected List<Resource> resources;

}
