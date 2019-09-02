package com.github.attemper.executor.model;

import com.github.attemper.common.exception.RTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class FtpInfo {

    private String ip;

    private int port = 21;

    private String username;

    private String password;

    private int retryCount = 3;

    private int timeout = 5;  //second

    public String getIp() {
        return ip;
    }

    public FtpInfo setIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            throw new RTException(2560);
        }
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public FtpInfo setPort(String portStr) {
        try {
            if (StringUtils.isNotBlank(portStr)) {
                this.port = Integer.parseInt(portStr);
            }
        } catch (NumberFormatException e) {
            log.error(portStr, e);
        }
        return this;
    }

    public String getUsername() {
        return username;
    }

    public FtpInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public FtpInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public FtpInfo setRetryCount(String retryCountStr) {
        try {
            if (StringUtils.isNotBlank(retryCountStr)) {
                this.retryCount = Integer.parseInt(retryCountStr);
                if (this.retryCount < 0) {
                    this.retryCount = 0;
                }
            }
        } catch (NumberFormatException e) {
            log.error(retryCountStr, e);
        }
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public FtpInfo setTimeout(String timeoutStr) {
        try {
            if (StringUtils.isNotBlank(timeoutStr)) {
                this.timeout = Integer.parseInt(timeoutStr);
                if (this.timeout <= 0) {
                    this.timeout = 5;
                }
            }
        } catch (NumberFormatException e) {
            log.error(timeoutStr, e);
        }
        return this;
    }
}
