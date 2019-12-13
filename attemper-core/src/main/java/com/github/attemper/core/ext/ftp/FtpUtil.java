package com.github.attemper.core.ext.ftp;

import com.github.attemper.common.exception.RTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;

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
                Thread.sleep(2000L);
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

    /**
     * validate reply code
     * @param ftpClient
     */
    public static void validateReplyCode(FTPClient ftpClient) {
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            throw new RTException(ftpClient.getReplyCode(), ftpClient.getReplyString());
        }
    }

    /**
     * list files and validate reply code
     * @param ftpClient
     * @param pathname
     * @return
     */
    public static FTPFile[] listFiles(FTPClient ftpClient, String pathname) {
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(pathname);
            validateReplyCode(ftpClient);
            return ftpFiles;
        } catch (IOException e) {
            throw new RTException(2563, e);
        }
    }
}
