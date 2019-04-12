package com.sse.attemper.common.result.sys.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceInfo {

    /**
     * tenant id
     */
    protected String id;

    protected String baseUrl;

    protected String contextPath;
}
