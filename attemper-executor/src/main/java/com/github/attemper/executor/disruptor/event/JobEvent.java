package com.github.attemper.executor.disruptor.event;

import com.github.attemper.common.param.executor.JobInvokingParam;

public class JobEvent {

    private JobInvokingParam param;


    public JobEvent() {
    }

    public JobInvokingParam getParam() {
        return param;
    }

    public void setParam(JobInvokingParam param) {
        this.param = param;
    }

    public void clear() {
        param = null;
    }
}
