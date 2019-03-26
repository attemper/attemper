package com.sse.attemper.scheduler.job;

import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingRequest;
import com.sse.atemper.grpc.invoking.JobInvokingProto.JobInvokingResponse;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc;
import com.sse.atemper.grpc.invoking.JobInvokingServiceGrpc.JobInvokingServiceBlockingStub;
import com.sse.attemper.config.bean.ContextBeanAware;
import io.grpc.Channel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExecutableJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Channel channel = ContextBeanAware.getBean(Channel.class);
        JobInvokingServiceBlockingStub stub = JobInvokingServiceGrpc.newBlockingStub(channel);
        JobInvokingResponse helloResponse = stub.invoke(
                JobInvokingRequest.newBuilder()
                        .setSequenceNo(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS").format(LocalDateTime.now()))
                        .setTriggerName(context.getTrigger().getKey().getName())
                        .setJobName(context.getJobDetail().getKey().getName())
                        .setTenantId(context.getJobDetail().getKey().getGroup())
                        .build());
        System.out.println(helloResponse);
    }
}
