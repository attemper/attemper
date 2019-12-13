package com.github.attemper.core.service.tool;

import com.github.attemper.common.constant.APIPath;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.ArgType;
import com.github.attemper.common.enums.PredefinedDateArgType;
import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.tool.server.ServerInfoGetParam;
import com.github.attemper.common.result.CommonResult;
import com.github.attemper.common.result.dispatch.tool.ServerInfoResult;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.base.util.ServletUtil;
import com.github.attemper.core.ext.server.*;
import com.github.attemper.sys.service.BaseServiceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import oshi.SystemInfo;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;

@Slf4j
@Service
public class ToolService extends BaseServiceAdapter {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AppProperties appProperties;

    public String[] listTimeZone() {
        return TimeZone.getAvailableIDs();
    }

    public Boolean ping(String uri, Integer type) {
        if (uri == null) {
            log.error("uri is null");
            return false;
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug(uri);
            }
            uri = uri.replace("https://", "").replace("http://", "");
            if (type == UriType.DiscoveryClient.getType()) {
                if (log.isDebugEnabled()) {
                    log.debug("discovery client");
                }
                List<ServiceInstance> instances = discoveryClient.getInstances(uri);
                if (instances.isEmpty()) {
                    throw new RTException(800);
                }
                for (ServiceInstance item : instances) {
                    if (!ping(item.getUri().toString(), UriType.IpWithPort.getType())) {
                        return false;
                    }
                }
            } else if (type == UriType.IpWithPort.getType()) {
                if (log.isDebugEnabled()) {
                    log.debug("ip:port");
                }
                String[] arr = uri.split(":");
                SocketAddress socketAddr = new InetSocketAddress(arr[0], Integer.parseInt(arr[1]));
                new Socket().connect(socketAddr, 10000);
            } else if (type == UriType.DomainName.getType()) {
                if (log.isDebugEnabled()) {
                    log.debug("domain");
                }
                InetAddress.getByName(uri).isReachable(10000);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public List<ServiceInstance> listSchedulerService() {
        return listService(appProperties.getScheduler().getName());
    }

    public List<ServiceInstance> listExecutorService() {
        return listService(appProperties.getExecutor().getName());
    }

    private List<ServiceInstance> listService(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

    public Map<String, Object> requestServerInfo(ServerInfoGetParam param) {
        String url;
        if ("scheduler".equals(param.getSource())) {
            url = param.getAddress() + appProperties.getScheduler().getContextPath() + APIPath.CommonPath.SERVER_INFO;
        } else if ("executor".equals(param.getSource())) {
            url = param.getAddress() + appProperties.getExecutor().getContextPath() + APIPath.CommonPath.SERVER_INFO;
        } else {
            throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), new IllegalArgumentException(param.getSource()));
        }
        CommonResult<Map<String, Object>> commonResult = WebClient.builder().build()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(CommonConstants.token, ServletUtil.getHeader(CommonConstants.token))
                .retrieve()
                .onStatus(s -> s == HttpStatus.UNAUTHORIZED, resp -> Mono.error(new RTException(HttpStatus.UNAUTHORIZED.value(), resp.statusCode().getReasonPhrase())))
                .bodyToMono(CommonResult.class)
                .doOnError(Exception.class, err -> {
                    if (err instanceof RTException) {
                        throw (RTException) err;
                    } else if (err.getMessage().contains("Connection refused")) {
                        throw new RTException(3009, err);
                    }
                    throw new RTException(err);
                })
                .block();
        if (commonResult.getCode() != 200) {
            throw new RTException(commonResult.getCode(), commonResult.getMsg());
        }
        return commonResult.getResult();
    }

    public ServerInfoResult responseServerInfo() {
        SystemInfo systemInfo = new SystemInfo();
        return new ServerInfoResult()
                .setCpu(CpuUtil.getCpuStatus(systemInfo))
                .setMemory(MemoryUtil.getMemoryStatus(systemInfo))
                .setMachine(MachineUtil.getMachineInfo())
                .setJvm(JvmUtil.getJvmStatus())
                .setResource(ResourceUtil.getResourceInfo())
                .setFile(FileStoreUtil.getFileStoreInfo(systemInfo));
    }

    public Date getCurrentTime() {
        return new Date();
    }

    public List<Map<String, Object>> listArgType() {
        List<Map<String, Object>> list = new ArrayList<>(20);
        for (ArgType type : ArgType.values()) {
            Map<String, Object> map = new HashMap<>(2);
            map.put(CommonConstants.label, type.getName());
            map.put(CommonConstants.value, type.getValue());
            list.add(map);
        }
        return list;
    }

    public List<String> listTradeDateUnit() {
        List<String> values = new ArrayList<>(10);
        for (PredefinedDateArgType type : PredefinedDateArgType.values()) {
            values.add(type.getValue());
        }
        return values;
    }
}
