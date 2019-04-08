package com.sse.attemper.common.result.sys.user;

import com.sse.attemper.common.result.sys.tenant.Tenant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfo {

    /** 用户 */
    protected User user;

    /** 被该用户管理的租户 */
    protected Tenant tenant;

}
