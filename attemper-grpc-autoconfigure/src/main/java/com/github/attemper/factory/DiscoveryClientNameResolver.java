package com.github.attemper.factory;

import com.github.attemper.properties.GrpcClientProperties;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * resolve discovery server'address
 */
public class DiscoveryClientNameResolver extends NameResolver {

    private Listener listener;
    private final String name;
    private final DiscoveryClient client;
    private final GrpcClientProperties properties;

    public DiscoveryClientNameResolver(String name, DiscoveryClient client, GrpcClientProperties properties) {
        this.name = name;
        this.client = client;
        this.properties = properties;
    }

    @Override
    public String getServiceAuthority() {
        return name;
    }

    @Override
    public void start(Listener listener) {
        this.listener = listener;
        this.refresh();
    }

    @Override
    public void refresh() {
        List<SocketAddress> socketAddresses = new ArrayList<>();
        for (ServiceInstance serviceInstance : client.getInstances(name)) {
            socketAddresses.add(new InetSocketAddress(serviceInstance.getHost(), this.properties.getServer().getPort()));
        }
        List<EquivalentAddressGroup> equivalentAddressGroups = new ArrayList<>();
        equivalentAddressGroups.add(new EquivalentAddressGroup(socketAddresses));
        this.listener.onAddresses(equivalentAddressGroups, Attributes.EMPTY);
    }

    @Override
    public void shutdown() {
    }
}
