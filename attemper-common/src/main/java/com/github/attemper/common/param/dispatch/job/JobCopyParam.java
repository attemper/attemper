package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class JobCopyParam implements CommonParam {

    protected String jobName;

    protected Integer version;

    protected JobSaveParam targetJobParam;

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        if (targetJobParam == null) {
            return "6081";
        }
        return targetJobParam.validate();
    }

}
