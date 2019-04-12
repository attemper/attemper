package com.sse.attemper.core.service.tool;

import com.sse.attemper.common.exception.RTException;
import com.sse.attemper.common.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.TimeZone;

@Slf4j
@Service
public class ToolService {

    public String[] listTimeZone() {
        return TimeZone.getAvailableIDs();
    }

    public Void ping(String baseUrl) {
        try {
            if (baseUrl == null) {
                throw new RTException(500, "baseUrl is null");
            }
            log.debug(baseUrl);
            baseUrl = baseUrl.replace("https://", "").replace("http://", "");
            String[] arr = baseUrl.split(":");
            if (arr.length > 1 && NumberUtil.isPositiveNumber(arr[arr.length - 1])) {
                log.debug("ip:port");
                SocketAddress socketAddr = new InetSocketAddress(arr[arr.length - 2].replace("//", ""), Integer.parseInt(arr[arr.length - 1]));
                Socket socket = new Socket();
                socket.connect(socketAddr, 5000);
            } else {
                log.debug("domain");
                InetAddress inetAddress = InetAddress.getByName(baseUrl);
                inetAddress.isReachable(5000);
            }
            return null;
        } catch (Exception e) {
            throw new RTException(500, e);
        }
    }
}
