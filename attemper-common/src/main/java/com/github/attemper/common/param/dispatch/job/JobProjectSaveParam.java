package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
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
