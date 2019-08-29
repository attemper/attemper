package com.github.attemper.executor.task.ftp.sse.impl;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.executor.task.ftp.sse.StorageGatewayTask;
import com.github.attemper.executor.util.FtpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.commons.utils.IoUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class UploadFtpTask extends StorageGatewayTask {

    @Override
    protected void executeInternal(DelegateExecution execution, FTPClient ftpClient) {
        UploadFileInfo fileInfo = buildFileInfo(execution);
        List<String> rows = selectForString(execution);
        appendLogText(execution, "select " + rows.size() + ' ');
        StringBuilder fileContent = new StringBuilder();
        if (rows.size() == 0) {
            return;
        } else {
            if (fileInfo.isFormatField()) {
                String paddingMsg = fileInfo.getFieldPaddingMsg();
                if (StringUtils.isBlank(paddingMsg)) {
                    throw new RTException(2570, fileInfo.getFileName());
                }
                String[] formats = paddingMsg.split(fileInfo.getSeparator());
                for (String row : rows) {
                    String[] cols = row.split(fileInfo.getSeparator());
                    for (int i = 0; i < cols.length; i++) {
                        String formattedStr = formatColumn(cols[i], formats[i]);
                        fileContent.append(formattedStr + '|');
                    }
                    fileContent.deleteCharAt(fileContent.length() - 1).append("\r\n");
                }
            } else {
                for (String row : rows) {
                    fileContent.append(row).append("\r\n");
                }
            }
        }
        if (fileInfo.isIncludeFlag()) {
            //String flagContent = toFlagContent();
        }
        int code;
        try {
            ftpClient.changeWorkingDirectory(fileInfo.getRemotePath());
            ftpClient.storeFile(fileInfo.getRemotePath() + '/' + fileInfo.getFileName() + '.' + fileInfo.getDataSuffix(), IoUtil.stringAsInputStream(fileContent.toString()));
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
/*
    private String toFlagContent(FileInfo fileInfo) {
        StringBuilder sb = new StringBuilder();
        if (fileInfo.getFlagType() == 3) {
            sb.append(fileInfo.getFileName()).append(fileInfo.getSeparator())
        } else {

        }
    }*/

    private String formatColumn(String source, String format) {
        String[] arr = format.split(":");
        String direction = arr[0];
        int charCount = Integer.parseInt(arr[1]);
        int size = source == null ? 0 : source.replaceAll("[^\\x00-\\xff]", "**").length();
        if ("R".equalsIgnoreCase(direction)) {
            for (int i=0; i < charCount - size; i++) {
                source += " ";
            }
        } else {
            for (int i=0; i < charCount - size; i++) {
                source = " " + source;
            }
        }
        return source;
    }

    /**
     * @param execution
     * @return xxx|xxx|xxx|xxx
     */
    private List<String> selectForString(DelegateExecution execution) {
        return injectJdbcTemplate(execution).queryForList(injectSql(execution), String.class);
    }

    @Override
    protected <T extends FileInfo> T buildSubFileInfo(DelegateExecution execution) {
        return (T) new UploadFileInfo()
                .setFormatField(!CommonConstants.FALSE.equals(execution.getVariable(FORMAT_FIELD)))
                .setFieldPaddingMsg((String) execution.getVariable(FIELD_PADDING_MSG));
    }

    private static class UploadFileInfo extends FileInfo {

        private boolean formatField;

        private String fieldPaddingMsg;

        public boolean isFormatField() {
            return formatField;
        }

        public UploadFileInfo setFormatField(boolean formatField) {
            this.formatField = formatField;
            return this;
        }

        public String getFieldPaddingMsg() {
            return fieldPaddingMsg;
        }

        public UploadFileInfo setFieldPaddingMsg(String fieldPaddingMsg) {
            this.fieldPaddingMsg = fieldPaddingMsg;
            return this;
        }
    }

    protected static final String FORMAT_FIELD = "format_field";

    protected static final String FIELD_PADDING_MSG = "field_padding_msg";
}
