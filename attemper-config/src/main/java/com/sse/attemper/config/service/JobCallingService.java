package com.sse.attemper.config.service;

import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.sdk.common.result.CommonResult;
import io.grpc.Channel;
import org.springframework.stereotype.Service;

@Service
public class JobCallingService {

    public CommonResult<Void> invoke(String sequenceNo, String jobName, String tenantId) {
        return this.invoke(sequenceNo, jobName, null, tenantId);
    }

    public CommonResult<Void> invoke(String sequenceNo, String jobName, String triggerName, String tenantId) {
        Channel channel = ContextBeanAware.getBean(Channel.class);
        JobInvokingServiceGrpc.JobInvokingServiceBlockingStub stub = JobInvokingServiceGrpc.newBlockingStub(channel);
        JobInvokingProto.JobInvokingResponse helloResponse = stub.invoke(
                JobInvokingProto.JobInvokingRequest.newBuilder()
                        .setSequenceNo(sequenceNo)
                        .setJobName(jobName)
                        .setTriggerName(triggerName)
                        .setTenantId(tenantId)
                        .build());
        return CommonResult.put(helloResponse.getCode(), helloResponse.getMsg());
    }
}
