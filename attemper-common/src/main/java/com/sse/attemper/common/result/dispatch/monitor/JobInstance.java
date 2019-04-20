package com.sse.attemper.common.result.dispatch.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInstance {

    protected String id;

    protected String procInstId;

    protected String triggerName;

    protected Integer status;

    protected String logText;
}
