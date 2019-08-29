package com.github.attemper.executor.task.ftp.sse.impl;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.executor.task.ftp.sse.StorageGatewayTask;
import com.github.attemper.executor.util.FtpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DownloadFtpTask extends StorageGatewayTask {

    @Override
    protected void executeInternal(DelegateExecution execution, FTPClient ftpClient) {
        DownloadFileInfo fileInfo = buildFileInfo(execution);
        int code;
        try {
            ftpClient.changeWorkingDirectory(fileInfo.getRemotePath());
            FlagField flagField = null;
            if (fileInfo.isIncludeFlag()) {
                String flagFileName = fileInfo.getFileName() + '.' + fileInfo.getFlagSuffix();
                String flagContent = transFileToContent(ftpClient, flagFileName, fileInfo.getCharsetName());
                if (StringUtils.isBlank(flagContent)) {
                    throw new RTException(2563, flagFileName);
                }
                flagField = toFlagField(flagContent, fileInfo);
            }
            String dataFileName = fileInfo.getFileName() + '.' + fileInfo.getDataSuffix();
            String dataContent = transFileToContent(ftpClient, dataFileName, fileInfo.getCharsetName());
            if (StringUtils.isBlank(dataContent)) {
                throw new RTException(2566, dataFileName);
            }
            List<Object[]> data = toData(dataContent, fileInfo);
            if (flagField != null) {
                check(dataContent, data.size(), fileInfo, flagField);
            }
            int[] insertCount = insert(execution, data);
            appendLogText(execution, INSERT + ' ' + insertCount.length + ' ');
        } catch (RTException e) {
            saveLogKey(execution, e.getCode());
            throw e;
        } catch (IOException e) {
            code = 1100;
            saveLogKey(execution, code);
            throw new RTException(code, e);
        } catch (Exception e) {
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            saveLogKey(execution, code);
            throw new RTException(code, e);
        } finally {
            try {
                FtpUtil.closeFtpClient(ftpClient);
            } catch (RTException e) {
                saveLogKey(execution,e.getCode());
                throw e;
            }
        }
    }

    private void check(String dataContent, int dataRowCount, DownloadFileInfo fileInfo, FlagField flagField) {
        if (dataRowCount != flagField.getRowCount()) { //check row count
            throw new RTException(2567, "row count:" + dataRowCount + "!=" + flagField.getRowCount());
        } else if (flagField.getSize() < Integer.MAX_VALUE &&
                dataContent.replaceAll("[^\\x00-\\xff]", "**").getBytes().length != flagField.getSize()) { //check content bytes
            throw new RTException(2567, "file size:" + dataContent.getBytes().length + "!=" + flagField.getSize());
        } else if (!StringUtils.equalsIgnoreCase(fileInfo.getFileName() + '.' + fileInfo.getDataSuffix(), flagField.getDataFileName())) {//check file name
            throw new RTException(2567, "file name:" + (fileInfo.getFileName() + '.' + fileInfo.getDataSuffix()) + "!=" + flagField.getDataFileName());
        } else if (fileInfo.getFlagType() == 3){
            /*if (!StringUtils.equalsIgnoreCase(target, flagField.getEncryption())) {
                throw new RTException(2567, "md5:" + (target + "!=" + flagField.getEncryption()).toLowerCase());
            }*/
        }
    }

    private List<Object[]> toData(String dataContent, DownloadFileInfo fileInfo) {
        List<Object[]> data = new ArrayList<>();
        String[] rows = dataContent.split("\r\n");
        if (fileInfo.isCheck()) {
            if (StringUtils.isBlank(fileInfo.getCheckItems())) {
                throw new RTException(2569, fileInfo.getFileName());
            }
            for (int i = 0; i < rows.length; i++) {
                String row = rows[i];
                if (StringUtils.isNotBlank(row)) {
                    if (row.replaceAll("[^\\x00-\\xff]", "**").matches(fileInfo.getCheckItems())) {
                        String[] cols = row.split(fileInfo.getSeparator());
                        for (int j = 0; j < cols.length; j++) {
                            cols[j] = StringUtils.trimToEmpty(cols[j]);
                        }
                        data.add(cols);
                    } else {
                        throw new RTException(2568, "line num:" + (i + 1) + ",row:" + row + ",regex:" + fileInfo.getCheckItems());
                    }
                } else {
                    log.error("line num {} is blank, file name:{}", (i + 1), fileInfo.getFileName());
                }
            }
        } else {
            for (int i = 0; i < rows.length; i++) {
                String row = rows[i];
                if (StringUtils.isNotBlank(row)) {
                    String[] cols = row.split(fileInfo.getSeparator());
                    data.add(cols);
                } else {
                    log.error("line num {} is blank, file name:{}", (i + 1), fileInfo.getFileName());
                }
            }
        }
        return data;
    }

    private FlagField toFlagField(String flagContent, FileInfo fileInfo) {
        String[] fields = flagContent.split(fileInfo.getSeparator());
        try {
            FlagField flagField = new FlagField()
                    .setDataFileName(StringUtils.trimToNull(fields[0]));
            if (fileInfo.getFlagType() == 3) {
                flagField
                        .setSize(Long.parseLong(StringUtils.trimToNull(fields[1])))
                        .setCreateDate(StringUtils.trimToNull(fields[2]))
                        .setCreateTime(StringUtils.trimToNull(fields[3]))
                        .setRowCount(Integer.parseInt(StringUtils.trimToNull(fields[4])))
                        .setEncryption(StringUtils.trimToNull(fields[5]));
            } else {
                flagField
                        .setCreateDate(StringUtils.trimToNull(fields[1]))
                        .setCreateTime(StringUtils.trimToNull(fields[2]))
                        .setRowCount(Integer.parseInt(StringUtils.trimToNull(fields[3])))
                        .setSize(Long.parseLong(StringUtils.trimToNull(fields[4])));
            }
            return flagField;
        } catch (NumberFormatException e) {
            throw new RTException(2565, e);
        }
    }

    @Override
    protected <T extends FileInfo> T buildSubFileInfo(DelegateExecution execution) {
        return (T) new DownloadFileInfo()
                .setCheck(!CommonConstants.FALSE.equals(execution.getVariable(CHECK)))
                .setCheckItems((String) execution.getVariable(CHECK_ITEMS));
    }

    private static class DownloadFileInfo extends FileInfo {

        private boolean check;

        private String checkItems;

        public boolean isCheck() {
            return check;
        }

        public DownloadFileInfo setCheck(boolean check) {
            this.check = check;
            return this;
        }

        public String getCheckItems() {
            return checkItems;
        }

        public DownloadFileInfo setCheckItems(String checkItems) {
            if (StringUtils.isNotBlank(checkItems)) {
                this.checkItems = checkItems.replace("|", "\\|");
            }
            return this;
        }
    }

    protected static final String CHECK = "check";

    protected static final String CHECK_ITEMS = "check_items";
}
