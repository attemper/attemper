package com.sse.attemper.scheduler.autoconfigure;

import io.grpc.Channel;

public interface ChannelFactory {

  Channel create(String name);
}
