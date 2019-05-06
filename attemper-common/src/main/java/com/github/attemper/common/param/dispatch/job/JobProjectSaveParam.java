package com.github.attemper.common.param.dispatch.job;

import com.github.attemper.common.param.CommonParam;
import com.github.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
