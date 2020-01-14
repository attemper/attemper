package com.github.attemper.executor.task.ftp;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.core.ext.model.FileParam;
import com.github.attemper.core.util.FtpUtil;
import com.github.attemper.core.util.FileUtil;
import com.github.attemper.executor.task.ftp.internal.FtpTask;
import org.apache.commons.net.ftp.FTPClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class Local2FtpTask extends FtpTask {
    @Override
    protected void executeInternal(DelegateExecution execution, FTPClient ftpClient, FileParam fileParam) {
        appendLogText(execution, 10220);
        File localFile = FileUtil.getFile(fileParam.getLocalDirectory(), fileParam.getRemotePath(), fileParam.getFileNameWithSuffix());
        changeWorkingDirectory(ftpClient, fileParam.getRemotePath());
        appendLogText(execution, 10206, fileParam.getRemotePath());
        try (InputStream is = new FileInputStream(localFile)){
            boolean storedSuccess = ftpClient.storeFile(fileParam.getFileNameWithSuffix(), is);
            FtpUtil.validateReplyCode(ftpClient);
            if (!storedSuccess) {
                throw new RTException(2566, fileParam.getRemotePath() + fileParam.getFileNameWithSuffix());
            }
            appendLogText(execution, 10221, fileParam.getFileNameWithSuffix());
        } catch (IOException e) {
            throw new RTException(2565, e);
        }
    }
}
