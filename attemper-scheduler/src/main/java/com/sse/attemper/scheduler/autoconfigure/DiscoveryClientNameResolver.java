package com.sse.attemper.scheduler.autoconfigure;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryClientNameResolver extends NameResolver {
	private final String name;
	private final DiscoveryClient client;
	private Listener listener;

	public DiscoveryClientNameResolver(String name, DiscoveryClient client) {
		this.name = name;
		this.client = client;
	}
	@Override
	public String getServiceAuthority() {
		return name;
	}

	@Override
	public void start(Listener listener) {
		this.listener = listener;
		refresh();
	}

	@Override
	public void refresh() {
		List<SocketAddress> socketAddresses = new ArrayList<>();
		for (ServiceInstance serviceInstance : client.getInstances(name)) {
			socketAddresses.add(new InetSocketAddress(serviceInstance.getHost(), serviceInstance.getPort() + 1));
		}
		List<EquivalentAddressGroup> equivalentAddressGroups = new ArrayList<>();
		equivalentAddressGroups.add(new EquivalentAddressGroup(socketAddresses));
		this.listener.onAddresses(equivalentAddressGroups, Attributes.EMPTY);
	}

	@Override
	public void shutdown() {
	}
}
