package com.github.attemper.common.param.executor;

import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInvokingParam implements CommonParam {

    protected String id;

    protected String jobName;

    protected String triggerName;

    protected String tenantId;

    @Override
    public String validate() {
        return null;
    }
}
