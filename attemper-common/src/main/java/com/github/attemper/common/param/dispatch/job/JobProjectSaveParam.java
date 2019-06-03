package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class JobProjectSaveParam implements CommonParam {

    protected String jobName;

    protected String projectName;

    @Override
    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        return null;
    }
}
