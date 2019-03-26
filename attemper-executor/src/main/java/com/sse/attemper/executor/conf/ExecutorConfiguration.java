package com.sse.attemper.executor.conf;

import com.sse.attemper.executor.rpc.JobInvokingServiceImpl;
import com.sse.attemper.executor.shutdown.GracefulShutdown;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfiguration {

    @Value("${attemper.grpc.server.port}")
    private int rpcPort;

    @Bean
    public Server server() {
        return ServerBuilder.forPort(rpcPort)
                .addService(new JobInvokingServiceImpl())
                .build();
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory(Server server) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(new GracefulShutdown(server));
        return tomcat;
    }

}
