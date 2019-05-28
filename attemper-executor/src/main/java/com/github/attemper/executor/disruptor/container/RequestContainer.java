package com.github.attemper.executor.disruptor.container;

import com.github.attemper.common.param.executor.JobInvokingParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.executor.JobInvokingResult;

public class RequestContainer {

    private JobInvokingParam param;

    private CommonResult<JobInvokingResult> result;

    public RequestContainer() {
    }

    public JobInvokingParam getParam() {
        return param;
    }

    public void setParam(JobInvokingParam param) {
        this.param = param;
    }

    public CommonResult<JobInvokingResult> getResult() {
        return result;
    }

    public void setResult(CommonResult<JobInvokingResult> result) {
        this.result = result;
    }

    public void clear() {
        param = null;
        result = null;
    }
}
