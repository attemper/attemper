package com.github.attemper.executor.util;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.executor.model.FtpInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.TimeZone;

@Slf4j
public class FtpUtil {

    private static FTPClientConfig config;

    static {
        config = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        config.setServerTimeZoneId(TimeZone.getDefault().getID());
    }

    public static FTPClient getFtpClient(FtpInfo ftpInfo) {
        FTPClient ftpClient = new FTPClient();
        //ftpClient.setControlEncoding("GB2312");
        ftpClient.configure(config);
        ftpClient.setConnectTimeout(ftpInfo.getTimeout() * 1000);
        int retryCount = ftpInfo.getRetryCount();
        while (retryCount >= 0) {
            boolean skip = false;
            try {
                ftpClient.connect(ftpInfo.getIp(), ftpInfo.getPort());
            } catch (IOException e) {
                if (retryCount == 0) {
                    closeFtpClient(ftpClient);
                    throw new RTException(2550, ftpInfo.getIp() + ":" + ftpInfo.getPort());
                } else {
                    log.error(e.getMessage(), e);
                    skip = true;
                }
            }
            if (!skip) {
                try {
                    boolean logged = ftpClient.login(ftpInfo.getUsername(), ftpInfo.getPassword());
                    if (!logged && retryCount == 0) {
                        closeFtpClient(ftpClient);
                        throw new RTException(2551, ftpInfo.getUsername() + '/' + ftpInfo.getPassword());
                    }
                    if (logged) {
                        int replyCode = ftpClient.getReplyCode();
                        if (FTPReply.isPositiveCompletion(replyCode)) {
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            return ftpClient;
                        } else if (retryCount == 0){
                            closeFtpClient(ftpClient);
                            throw new RTException(2552, replyCode);
                        }
                    }
                } catch (IOException e) {
                    if (retryCount == 0) {
                        closeFtpClient(ftpClient);
                        throw new RTException(2551, ftpInfo.getUsername() + '/' + ftpInfo.getPassword());
                    } else {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            retryCount--;
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RTException(e);
            }
        }
        return null;
    }

    /**
     * close ftp client
     * @param ftpClient
     */
    public static void closeFtpClient(FTPClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.noop();
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new RTException(2554, e);
            }
        }
    }
}
