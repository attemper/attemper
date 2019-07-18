package com.github.attemper.common.param.dispatch.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class JobNameWithJsonArgParam implements CommonParam {

    protected String jobName;

    protected JsonNode jsonData;

    public String validate() {
        if(StringUtils.isBlank(jobName)){
            return "6000";
        }
        return null;
    }
}
