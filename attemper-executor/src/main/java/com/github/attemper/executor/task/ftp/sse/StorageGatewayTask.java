package com.github.attemper.executor.task.ftp.sse;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.executor.task.Databasing;
import com.github.attemper.executor.task.ftp.internal.FtpTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.nio.charset.Charset;

public abstract class StorageGatewayTask extends FtpTask implements Databasing {

    protected <T extends FileInfo> T buildFileInfo(DelegateExecution execution) {
        FileInfo fileInfo = buildSubFileInfo(execution);
        buildCommonFileInfo(execution, fileInfo);
        return (T) fileInfo;
    }

    protected abstract <T extends FileInfo> T buildSubFileInfo(DelegateExecution execution);

    private void buildCommonFileInfo(DelegateExecution execution, FileInfo fileInfo) {
        fileInfo
                .setRemotePath((String) execution.getVariable(REMOTE_PATH))
                .setFileName((String) execution.getVariable(FILE_NAME))
                .setDataSuffix((String) execution.getVariable(DATA_SUFFIX))
                .setIncludeFlag(!CommonConstants.FALSE.equals(execution.getVariable(INCLUDE_FLAG)))
                .setFlagSuffix((String) execution.getVariable(FLAG_SUFFIX))
                .setSeparator((String) execution.getVariable(SEPARATOR))
                .setFlagType((String) execution.getVariable(FLAG_TYPE))
                .setCharsetName((String) execution.getVariable(CHARSET_NAME));
    }

    @Slf4j
    protected static class FileInfo {
        private String remotePath;

        private String fileName;

        private String dataSuffix = "txt";

        private boolean includeFlag;

        private String flagSuffix = "flg";

        private String separator = "\\|";

        private int flagType = 3;

        private String charsetName = "GB2312";

        public String getRemotePath() {
            return remotePath;
        }

        public FileInfo setRemotePath(String remotePath) {
            this.remotePath = remotePath;
            return this;
        }

        public String getFileName() {
            return fileName;
        }

        public FileInfo setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public String getDataSuffix() {
            return dataSuffix;
        }

        public FileInfo setDataSuffix(String dataSuffix) {
            if (StringUtils.isNotBlank(dataSuffix)) {
                this.dataSuffix = dataSuffix;
            }
            return this;
        }

        public boolean isIncludeFlag() {
            return includeFlag;
        }

        public FileInfo setIncludeFlag(boolean includeFlag) {
            this.includeFlag = includeFlag;
            return this;
        }

        public String getFlagSuffix() {
            return flagSuffix;
        }

        public FileInfo setFlagSuffix(String flagSuffix) {
            if (StringUtils.isNotBlank(flagSuffix)) {
                this.flagSuffix = flagSuffix;
            }
            return this;
        }

        public String getSeparator() {
            return separator;
        }

        public FileInfo setSeparator(String separator) {
            if (StringUtils.isNotBlank(separator) && !separator.trim().equals("|")) {
                this.separator = separator;
            }
            return this;
        }

        public int getFlagType() {
            return flagType;
        }

        public FileInfo setFlagType(String flagType) {
            if (StringUtils.isNotBlank(flagType)) {
                try {
                    this.flagType = Integer.parseInt(flagType);
                } catch (NumberFormatException e) {
                    log.error("flagType=" + flagType, e);
                }
            }
            return this;
        }

        public String getCharsetName() {
            return charsetName;
        }

        public FileInfo setCharsetName(String charsetName) {
            if (StringUtils.isNotBlank(charsetName)
                    && Charset.isSupported(charsetName)) {
                this.charsetName = charsetName;
            }
            return this;
        }
    }

    public static class FlagField {

        /**
         * file name like xxx20190520.txt
         */
        private String dataFileName;

        /**
         * file size of byte
         */
        private long size;

        /**
         * date when the file created.like 20190520
         */
        private String createDate;

        /**
         * time when the file created like 010101
         */
        private String createTime;

        /**
         * the count of rows in the file
         */
        private int rowCount;

        /**
         * the md5 encryption of file
         */
        private String encryption;

        public String getDataFileName() {
            return dataFileName;
        }

        public FlagField setDataFileName(String dataFileName) {
            this.dataFileName = dataFileName;
            return this;
        }

        public long getSize() {
            return size;
        }

        public FlagField setSize(long size) {
            this.size = size;
            return this;
        }

        public String getCreateDate() {
            return createDate;
        }

        public FlagField setCreateDate(String createDate) {
            this.createDate = createDate;
            return this;
        }

        public String getCreateTime() {
            return createTime;
        }

        public FlagField setCreateTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public int getRowCount() {
            return rowCount;
        }

        public FlagField setRowCount(int rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        public String getEncryption() {
            return encryption;
        }

        public FlagField setEncryption(String encryption) {
            this.encryption = encryption;
            return this;
        }
    }

    private static final String REMOTE_PATH = "remote_path";

    private static final String FILE_NAME = "file_name";

    private static final String DATA_SUFFIX = "data_suffix";

    private static final String INCLUDE_FLAG = "include_flag";

    private static final String FLAG_SUFFIX = "flag_suffix";

    private static final String SEPARATOR = "separator";

    private static final String FLAG_TYPE = "flag_type";

    protected static final String CHARSET_NAME = "charset_name";
}
