package com.github.attemper.executor.task.ftp.internal;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.core.ext.model.FileParam;
import com.github.attemper.core.ext.ftp.FtpInfo;
import com.github.attemper.core.ext.ftp.FtpUtil;
import com.github.attemper.executor.task.ParentTask;
import com.github.attemper.executor.util.TaskParamUtil;
import org.apache.commons.lang.StringUtils;
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
        try {
            String mainPrefix = (String) execution.getVariable(CommonConstants.main + _FTP_NAME);
            if (StringUtils.isBlank(mainPrefix)) {
                mainPrefix = CommonConstants.main;
            }
            appendLogText(execution, 10200, mainPrefix);
            FtpInfo mainFtpInfo = (FtpInfo) ReflectUtil.reflectObj(FtpInfo.class, mainPrefix, execution.getVariables());
            ftpClient = FtpUtil.getFtpClient(mainFtpInfo);
            appendLogText(execution, 10201, mainPrefix);
        } catch (Exception e) {
            String backupPrefix = (String) execution.getVariable(CommonConstants.backup + _FTP_NAME);
            if (StringUtils.isBlank(backupPrefix)) {
                backupPrefix = CommonConstants.backup;
            }
            appendLogText(execution, 10202, backupPrefix);
            FtpInfo backupFtpInfo = (FtpInfo) ReflectUtil.reflectObj(FtpInfo.class, backupPrefix, execution.getVariables());
            ftpClient = FtpUtil.getFtpClient(backupFtpInfo);
            appendLogText(execution, 10203, backupPrefix);
        }
        if (ftpClient == null) {
            throw new RTException(2553);
        }
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

    private static final String _FTP_NAME = "_ftp_name"; // ftp name suffix
}
