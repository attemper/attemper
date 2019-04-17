package com.sse.attemper.config.scheduler.service;

import com.sse.atemper.grpc.invoking.JobInvokingProto;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc;
import com.sse.attemper.common.result.CommonResult;
import com.sse.attemper.config.scheduler.interceptor.HeaderClientInterceptor;
import com.sse.attemper.config.uuid.IdGenerator;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobCallingService {

    @Autowired
    private Channel originChannel;

    @Autowired
    private IdGenerator idGenerator;

    public CommonResult<Void> invoke(String jobName, String tenantId) {
        return this.invoke(jobName, "", tenantId);
    }

    public CommonResult<Void> invoke(String jobName, String triggerName, String tenantId) {
        ClientInterceptor interceptor = new HeaderClientInterceptor(tenantId);
        Channel channel = ClientInterceptors.intercept(originChannel, interceptor);
        JobInvokingServiceGrpc.JobInvokingServiceBlockingStub stub = JobInvokingServiceGrpc.newBlockingStub(channel);
        JobInvokingProto.JobInvokingResponse helloResponse = stub.invoke(
                JobInvokingProto.JobInvokingRequest.newBuilder()
                        .setId(idGenerator.getNextId())
                        .setJobName(jobName)
                        .setTriggerName(triggerName)
                        .setTenantId(tenantId)
                        .build());
        return CommonResult.put(helloResponse.getCode(), helloResponse.getMsg());
    }
}
