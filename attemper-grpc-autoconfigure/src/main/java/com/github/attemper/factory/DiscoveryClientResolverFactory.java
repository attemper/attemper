package com.github.attemper.factory;

import com.github.attemper.properties.GrpcClientProperties;
import io.grpc.NameResolver;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.Nullable;
import java.net.URI;

public class DiscoveryClientResolverFactory extends NameResolver.Factory {

    private final DiscoveryClient client;
    private final GrpcClientProperties properties;

    public DiscoveryClientResolverFactory(DiscoveryClient client, GrpcClientProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI uri, NameResolver.Helper helper) {
        return new DiscoveryClientNameResolver(uri.toString(), client, properties);
    }

    @Override
    public String getDefaultScheme() {
        return properties.getServer() != null ? properties.getServer().getName() : null;
    }
}
