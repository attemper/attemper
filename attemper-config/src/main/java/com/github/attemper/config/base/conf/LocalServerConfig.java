package com.github.attemper.config.base.conf;

import com.github.attemper.common.exception.RTException;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class LocalServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    public int getPort() {
        return this.serverPort;
    }

    public String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    public String getRequestPath() {
        return new StringBuilder("http://").append(getLocalHost()).append(":").append(getPort()).toString(); // TODO
    }
}
