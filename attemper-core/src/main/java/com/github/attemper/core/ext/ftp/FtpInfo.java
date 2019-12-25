package com.github.attemper.core.ext.ftp;

import com.github.attemper.common.exception.RTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class FtpInfo {

    private String ftpIp;

    private int ftpPort = 21;

    private String ftpUsername;

    private String ftpPassword;

    private int ftpRetryCount = 3;

    private int ftpTimeout = 5;

    public String getFtpIp() {
        return ftpIp;
    }

    public FtpInfo setFtpIp(String ftpIp) {
        if (StringUtils.isBlank(ftpIp)) {
            throw new RTException(1520);
        }
        this.ftpIp = ftpIp;
        return this;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public FtpInfo setFtpPort(String portStr) {
        if (StringUtils.isNotBlank(portStr)) {
            this.ftpPort = Integer.parseInt(portStr);
        }
        return this;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public FtpInfo setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
        return this;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public FtpInfo setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
        return this;
    }

    public int getFtpRetryCount() {
        return ftpRetryCount;
    }

    public FtpInfo setFtpRetryCount(String retryCountStr) {
        if (StringUtils.isNotBlank(retryCountStr)) {
            this.ftpRetryCount = Integer.parseInt(retryCountStr);
            if (this.ftpRetryCount < 0) {
                this.ftpRetryCount = 0;
            }
        }
        return this;
    }

    public int getFtpTimeout() {
        return ftpTimeout;
    }

    public FtpInfo setFtpTimeout(String timeoutStr) {
        if (StringUtils.isNotBlank(timeoutStr)) {
            this.ftpTimeout = Integer.parseInt(timeoutStr);
            if (this.ftpTimeout <= 0) {
                this.ftpTimeout = 5;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getFtpIp() != null) {
            sb.append("ip(" + getFtpIp() + ") ");
        }
        sb.append("port(" + getFtpPort() + ") ");
        if (getFtpUsername() != null) {
            sb.append("username(" + getFtpUsername() + ") ");
        }
        if (getFtpPassword() != null) {
            sb.append("password(" + getFtpPassword() + ") ");
        }
        sb.append("retryCount(" + getFtpRetryCount() + ") ");
        sb.append("timeout(" + getFtpTimeout() + ") ");
        return sb.toString();
    }
}
