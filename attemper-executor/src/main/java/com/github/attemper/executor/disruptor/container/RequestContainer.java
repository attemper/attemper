package com.github.attemper.executor.disruptor.container;

import com.github.attemper.common.param.executor.JobInvokingParam;

public class RequestContainer {

    private JobInvokingParam param;


    public RequestContainer() {
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
