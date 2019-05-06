package com.github.attemper.core.service.tool;

import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class ToolService {

    public String[] listTimeZone() {
        return TimeZone.getAvailableIDs();
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    public Boolean ping(String uri, Integer type) {
        try {
            if (uri == null) {
                throw new RTException(500, "uri is null");
            }
            log.debug(uri);
            uri = uri.replace("https://", "").replace("http://", "");
            if (type == UriType.DiscoveryClient.getType()) {
                log.debug("discovery client");
                List<ServiceInstance> instances = discoveryClient.getInstances(uri);
                if (instances.isEmpty()) {
                    throw new RTException(800);
                }
                instances.forEach(item -> ping(item.getUri().toString(), UriType.IpWithPort.getType()));
            } else if (type == UriType.IpWithPort.getType()) {
                log.debug("ip:port");
                String[] arr = uri.split(":");
                SocketAddress socketAddr = new InetSocketAddress(arr[0], Integer.parseInt(arr[1]));
                new Socket().connect(socketAddr, 5000);
            } else if (type == UriType.DomainName.getType()) {
                log.debug("domain");
                InetAddress.getByName(uri).isReachable(5000);
            }
            return true;
        } catch (Exception e) {
            throw new RTException(500, e);
        }
    }
}
