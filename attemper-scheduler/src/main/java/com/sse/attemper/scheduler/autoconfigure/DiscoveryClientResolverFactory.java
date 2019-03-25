package com.sse.attemper.scheduler.autoconfigure;

import io.grpc.NameResolver;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.Nullable;
import java.net.URI;

public class DiscoveryClientResolverFactory extends NameResolver.Factory {

    private final DiscoveryClient client;

    public DiscoveryClientResolverFactory(DiscoveryClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI uri, NameResolver.Helper helper) {
        return new DiscoveryClientNameResolver(uri.toString(), client);
    }

    @Override
    public String getDefaultScheme() {
        return null;
    }
}
