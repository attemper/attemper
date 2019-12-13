package com.github.attemper.core.ext.condition;

import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.enums.ConditionType;
import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.condition.sub.FtpFileConditionResult;
import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.ext.el.ELUtil;
import com.github.attemper.core.ext.ftp.FtpInfo;
import com.github.attemper.core.ext.ftp.FtpUtil;
import com.github.attemper.core.util.FileUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.util.Map;

@ConditionStrategyType(ConditionType.FTP_FILE)
public class FtpFileConditionStrategy implements ConditionStrategy<FtpFileConditionResult> {

    @Override
    public boolean validate(Condition condition, Map<String, Object> variableMap) {
        FtpFileConditionResult ftpFileCondition = parse(condition);
        if (variableMap == null || variableMap.size() == 0) {
            return false;
        }
        String prefix = ftpFileCondition.getPrefix();
        if (prefix == null) {
            prefix = CommonConstants.main;
        }
        FtpInfo ftpInfo = (FtpInfo) ReflectUtil.reflectObj(FtpInfo.class, prefix, variableMap);
        FTPClient ftpClient = FtpUtil.getFtpClient(ftpInfo);
        try {
            // because \ was an escape character,so \{xxx} can not be parsed
            String parsedPathName = ELUtil.parseExpression(
                    FileUtil.optimizePath(ftpFileCondition.getFilePath()),
                    variableMap);
            FTPFile[] remoteFiles = FtpUtil.listFiles(ftpClient, parsedPathName);
            if (remoteFiles.length == 0) {
                return false;
            }
            String parsedFileName = ELUtil.parseExpression(ftpFileCondition.getFileName(), variableMap);
            for (FTPFile remoteFile : remoteFiles) {
                if (remoteFile.getName().equals(parsedFileName)) {
                    return true;
                }
            }
            return false;
        } finally {
            FtpUtil.closeFtpClient(ftpClient);
        }
    }

    @Override
    public FtpFileConditionResult parse(Condition condition) {
        FtpFileConditionResult ftpFileConditionResult =
                BeanUtil.jsonStr2Bean(condition.getContent(), FtpFileConditionResult.class);
        ftpFileConditionResult
                .setConditionName(condition.getConditionName())
                .setTenantId(condition.getTenantId());
        return ftpFileConditionResult;
    }

}
