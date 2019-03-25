package com.sse.attemper.scheduler.conf;

import com.sse.attemper.scheduler.autoconfigure.DiscoveryClientChannelFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {

	@Bean
	public DiscoveryClientChannelFactory channelFactory(DiscoveryClient client) {
		return new DiscoveryClientChannelFactory(client);
	}

}
