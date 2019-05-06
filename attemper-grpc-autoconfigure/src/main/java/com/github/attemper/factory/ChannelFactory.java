package com.github.attemper.factory;

import io.grpc.Channel;

/**
 * channel factory
 */
public interface ChannelFactory {

    /**
     * create channel by server name
     *
     * @param name
     * @return
     */
    default Channel create(String name) {
        return null;
    }

    default Channel create() {
        return null;
    }
}
