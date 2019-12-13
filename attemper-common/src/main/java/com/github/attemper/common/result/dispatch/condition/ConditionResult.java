package com.github.attemper.common.result.dispatch.condition;

import com.github.attemper.common.result.dispatch.condition.sub.FtpFileConditionResult;
import com.github.attemper.common.result.dispatch.condition.sub.LocalFileConditionResult;
import com.github.attemper.common.result.dispatch.condition.sub.SqlConditionResult;
import lombok.ToString;

import java.util.List;

@ToString
public class ConditionResult {

    protected List<FtpFileConditionResult> ftpFileConditions;

    protected List<LocalFileConditionResult> localFileConditions;

    protected List<SqlConditionResult> sqlConditions;

    public List<FtpFileConditionResult> getFtpFileConditions() {
        return ftpFileConditions;
    }

    public ConditionResult setFtpFileConditions(List<FtpFileConditionResult> ftpFileConditions) {
        this.ftpFileConditions = ftpFileConditions;
        return this;
    }

    public List<LocalFileConditionResult> getLocalFileConditions() {
        return localFileConditions;
    }

    public ConditionResult setLocalFileConditions(List<LocalFileConditionResult> localFileConditions) {
        this.localFileConditions = localFileConditions;
        return this;
    }

    public List<SqlConditionResult> getSqlConditions() {
        return sqlConditions;
    }

    public ConditionResult setSqlConditions(List<SqlConditionResult> sqlConditions) {
        this.sqlConditions = sqlConditions;
        return this;
    }
}
