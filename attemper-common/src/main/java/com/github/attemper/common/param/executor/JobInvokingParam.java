package com.github.attemper.common.param.executor;

import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInvokingParam implements CommonParam {

    protected String id;

    protected String jobName;

    protected String triggerName;

    protected String tenantId;

    protected Map<String, Object> dataMap;

    @Override
    public String validate() {
        return null;
    }
}
