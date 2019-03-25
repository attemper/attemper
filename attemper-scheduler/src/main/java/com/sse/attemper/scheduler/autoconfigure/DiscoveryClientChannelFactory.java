package com.sse.attemper.scheduler.autoconfigure;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class DiscoveryClientChannelFactory implements ChannelFactory {

  private final DiscoveryClient client;

  public DiscoveryClientChannelFactory(DiscoveryClient client) {
    this.client = client;
  }

  @Override
  public Channel create(String name) {
    return ManagedChannelBuilder.forTarget(name)
            .defaultLoadBalancingPolicy("round_robin")
            .nameResolverFactory(new DiscoveryClientResolverFactory(client))
            .usePlaintext()
            .build();
  }
}
