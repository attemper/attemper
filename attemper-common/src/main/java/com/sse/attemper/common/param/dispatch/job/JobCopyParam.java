package com.sse.attemper.common.param.dispatch.job;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCopyParam implements CommonParam {

    protected String jobName;

    protected Integer reversion;

    protected JobSaveParam targetJobParam;

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        if (reversion == null || reversion <= 0) {
            return "6009";
        }
        if (targetJobParam == null) {
            return "6081";
        }
        return targetJobParam.validate();
    }

}
