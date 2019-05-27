package com.github.attemper.core.service.tool;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.config.base.property.AppProperties;
import com.zaxxer.hikari.HikariDataSource;
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

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AppProperties appProperties;

    public String[] listTimeZone() {
        return TimeZone.getAvailableIDs();
    }

    public Boolean ping(String uri, Integer type) {
        try {
            if (uri == null) {
                throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, "uri is null");
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
            throw new RTException(CommonConstants.INTERNAL_SERVER_ERROR, e);
        }
    }

    public List<ServiceInstance> listExecutorService() {
        return listService(appProperties.getExecutor().getName());
    }

    private List<ServiceInstance> listService(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

    public String testConnection(String driverClassName, String jdbcUrl, String userName, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        try {
            dataSource.setMinimumIdle(2);
            dataSource.setMaximumPoolSize(2);
            dataSource.setDriverClassName(driverClassName);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            dataSource.getConnection();
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        } finally {
            dataSource.close();
        }
    }
}
