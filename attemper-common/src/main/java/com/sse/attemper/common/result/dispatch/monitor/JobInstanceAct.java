package com.sse.attemper.common.result.dispatch.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceAct {

    protected String id;

    protected String actInstId;

    protected Integer status;

    protected String code;

    protected String logText;

}
