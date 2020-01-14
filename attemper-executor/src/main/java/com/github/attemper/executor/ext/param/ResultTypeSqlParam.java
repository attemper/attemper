package com.github.attemper.executor.ext.param;

import com.github.attemper.executor.ext.enums.ResultDataType;
import org.apache.commons.lang.StringUtils;

public class ResultTypeSqlParam extends SqlParam {

    protected String resultType;

    public int getResultType() {
        if (StringUtils.isBlank(resultType)) {
            return ResultDataType.OBJECT.getValue();
        }
        return Integer.parseInt(resultType);
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString()).append("resultType(" + getResultType() + ") ").toString();
    }
}
