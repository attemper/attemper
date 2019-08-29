package com.github.attemper.executor.task.ftp.internal;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.executor.model.FtpInfo;
import com.github.attemper.executor.task.ParentTask;
import com.github.attemper.executor.util.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.commons.utils.IoUtil;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Task to handle FTP Client
 */
public abstract class FtpTask extends ParentTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        FTPClient ftpClient = injectFtpClient(execution);
        executeInternal(execution, ftpClient);
    }

    protected abstract void executeInternal(DelegateExecution execution, FTPClient ftpClient);

    private FTPClient injectFtpClient(DelegateExecution execution) {
        FTPClient ftpClient;
        FtpInfo mainFtpInfo = injectFtpInfo(execution.getVariables(), MAIN);
        try {
            ftpClient = FtpUtil.getFtpClient(mainFtpInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            appendLogText(execution, "Trying to connect to backup ftp ");
            try {
                FtpInfo backupFtpInfo = injectFtpInfo(execution.getVariables(), BACKUP);
                ftpClient = FtpUtil.getFtpClient(backupFtpInfo);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                if (ex instanceof RTException) {
                    RTException rte = (RTException) ex;
                    saveLogKey(execution, rte.getCode());
                } else {
                    saveLogKey(execution, HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                throw ex;
            }
        }
        if (ftpClient == null) {
            throw new RTException(2553);
        }
        return ftpClient;
    }

    /**
     * download file to string content
     * @param ftpClient
     * @param fileName
     * @param charsetName default is gb2312
     * @return
     */
    protected String transFileToContent(FTPClient ftpClient, String fileName, String charsetName) {
        try {
            InputStream is = ftpClient.retrieveFileStream(fileName);
            if (is == null) {
                throw new RTException(2562, fileName);
            } else {
                String content = IoUtil.readerAsString(new InputStreamReader(is, charsetName));
                is.close();
                ftpClient.completePendingCommand();
                return content;
            }
        } catch (IOException e) {
            log.error(fileName + e.getMessage(), e);
            throw new RTException(2561, e);
        }
    }

    private FtpInfo injectFtpInfo(Map<String, Object> variables, String prefix) {
        String ip = (String) variables.get(prefix + IP);
        String port = (String) variables.get(prefix + PORT);
        String username = (String) variables.get(prefix + USER_NAME);
        String password = (String) variables.get(prefix + PASSWORD);
        String retryCount = (String) variables.get(prefix + RETRY_COUNT);
        String timeout = (String) variables.get(prefix + TIMEOUT);
        return new FtpInfo()
                .setIp(ip)
                .setPort(port)
                .setUsername(username)
                .setPassword(password)
                .setRetryCount(retryCount)
                .setTimeout(timeout);
    }

    private static final String MAIN = "main_";  //main ftp config

    private static final String BACKUP = "backup_";  //backup ftp config

    private static final String IP = "ip";

    private static final String PORT = "port";

    private static final String USER_NAME = "username";

    private static final String PASSWORD = "password";

    private static final String RETRY_COUNT = "retryCount";

    private static final String TIMEOUT = "timeout";

}
