package com.github.attemper.common.param.scheduler;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Data
public class TriggerChangedParam implements CommonParam {

    protected String jobName;

    protected List<String> oldTriggerNames;

    public String validate() {
        if (StringUtils.isBlank(jobName)) {
            return "6000";
        }
        return null;
    }
}
