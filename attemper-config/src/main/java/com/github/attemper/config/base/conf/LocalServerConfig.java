package com.github.attemper.config.base.conf;

import com.github.attemper.config.base.property.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class LocalServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    @Autowired
    private AppProperties appProperties;

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
            throw new RuntimeException(e);
        }
    }

    public String getRequestPath() {
        return new StringBuilder(appProperties.getSchema()).append(getLocalHost()).append(":").append(getPort()).toString();
    }
}
