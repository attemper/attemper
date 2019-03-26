package com.sse.attemper.scheduler.conf;

import com.sse.attemper.autoconfigure.GrpcClientConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(
        GrpcClientConfiguration.class
)
@Configuration
public class SchedulerConfiguration {

}
