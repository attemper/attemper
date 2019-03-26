package com.sse.attemper.factory;

import com.sse.attemper.properties.GrpcClientProperties;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class DiscoveryClientChannelFactory implements ChannelFactory {

    private static final String POLICY_ROUND_ROBIN = "round_robin";

    private final DiscoveryClient client;
    private final GrpcClientProperties properties;

    public DiscoveryClientChannelFactory(DiscoveryClient client, GrpcClientProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    @Override
    public Channel create(String name) {
        return ManagedChannelBuilder.forTarget(name)
                .defaultLoadBalancingPolicy(POLICY_ROUND_ROBIN)
                .nameResolverFactory(new DiscoveryClientResolverFactory(client, properties))
                .usePlaintext()
                .build();
    }

    @Override
    public Channel create() {
        if (properties.getServer() == null || properties.getServer().getName() == null
                || "".equals(properties.getServer().getName().trim())) {
            throw new IllegalArgumentException("server name can not be null or empty");
        }
        return ManagedChannelBuilder.forTarget(properties.getServer().getName())
                .defaultLoadBalancingPolicy(POLICY_ROUND_ROBIN)
                .nameResolverFactory(new DiscoveryClientResolverFactory(client, properties))
                .usePlaintext()
                .build();
    }
}
