package com.github.attemper.executor.task.ftp.internal;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.core.ext.ftp.FtpInfo;
import com.github.attemper.core.util.FtpUtil;
import com.github.attemper.core.ext.model.FileParam;
import com.github.attemper.executor.task.ParentTask;
import com.github.attemper.executor.util.TaskParamUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.io.IOException;

/**
 * Task to handle FTP Client
 */
public abstract class FtpTask extends ParentTask {

    @Override
    public void executeIntern(DelegateExecution execution) {
        FTPClient ftpClient = injectFtpClient(execution);
        FileParam fileParam = TaskParamUtil.injectFileParam(execution.getVariables());
        appendLogText(execution, 10204, fileParam);
        try {
            executeInternal(execution, ftpClient, fileParam);
        } finally {
            FtpUtil.closeFtpClient(ftpClient);
        }
    }

    protected abstract void executeInternal(DelegateExecution execution, FTPClient ftpClient, FileParam fileParam);

    private FTPClient injectFtpClient(DelegateExecution execution) {
        FTPClient ftpClient;
        String ftpPrefix = (String) execution.getVariable(FTP_PREFIX);
        FtpInfo ftpInfo = ReflectUtil.reflectObj(new FtpInfo(), ftpPrefix, execution.getVariables());
        appendLogText(execution, 10200, ftpPrefix, ftpInfo);
        ftpClient = FtpUtil.getFtpClient(ftpInfo);
        if (ftpClient == null) {
            throw new RTException(2553);
        }
        appendLogText(execution, 10201, ftpPrefix);
        return ftpClient;
    }

    protected void changeWorkingDirectory(FTPClient ftpClient, String pathname) {
        try {
            ftpClient.changeWorkingDirectory(pathname);
            FtpUtil.validateReplyCode(ftpClient);
        } catch (IOException e) {
            throw new RTException(2564, e);
        }
    }

    private static final String FTP_PREFIX = "ftp_prefix";
}
