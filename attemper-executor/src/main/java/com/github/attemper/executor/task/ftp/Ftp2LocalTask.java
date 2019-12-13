package com.github.attemper.executor.task.ftp;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.core.ext.model.FileParam;
import com.github.attemper.core.ext.ftp.FtpUtil;
import com.github.attemper.core.util.FileUtil;
import com.github.attemper.executor.task.ftp.internal.FtpTask;
import org.apache.commons.net.ftp.FTPClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class Ftp2LocalTask extends FtpTask {
    @Override
    protected void executeInternal(DelegateExecution execution, FTPClient ftpClient, FileParam fileParam) {
        appendLogText(execution, 10205);
        File localFile = FileUtil.newFile(fileParam.getLocalDirectory(), fileParam.getRemotePath(), fileParam.getFileNameWithSuffix());
        changeWorkingDirectory(ftpClient, fileParam.getRemotePath());
        appendLogText(execution, 10206, fileParam.getRemotePath());
        try (OutputStream os = new FileOutputStream(localFile)){
            boolean successful = ftpClient.retrieveFile(fileParam.getFileNameWithSuffix(), os);
            FtpUtil.validateReplyCode(ftpClient);
            if (!successful) {
                throw new RTException(2568, fileParam.getRemotePath() + fileParam.getFileNameWithSuffix());
            }
            appendLogText(execution, 10207, fileParam.getFileNameWithSuffix());
            appendLogText(execution, 10208, localFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RTException(2567, e);
        }
    }

}
