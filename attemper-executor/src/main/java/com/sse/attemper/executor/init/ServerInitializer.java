package com.sse.attemper.executor.init;

import io.grpc.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ldang
 */
@Slf4j
@Component
public class ServerInitializer {

    @Autowired
    private Server server;

    @PostConstruct
    public void initGrpcServer() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("GRPC server starting...");
        }
        server.start();
        if (log.isDebugEnabled()) {
            log.debug("GRPC server started...");
        }
    }

}


