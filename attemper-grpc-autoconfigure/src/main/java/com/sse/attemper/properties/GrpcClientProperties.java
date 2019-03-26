package com.sse.attemper.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * properties
 */
@ConfigurationProperties(prefix = "attemper.grpc")
public class GrpcClientProperties {

    @NestedConfigurationProperty
    private Server server;

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public static class Server {

        /**
         * default name
         */
        private String name;

        /**
         * rpc server's port
         */
        private int port;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

}
