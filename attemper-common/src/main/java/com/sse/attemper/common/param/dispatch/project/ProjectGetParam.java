package com.sse.attemper.common.param.dispatch.project;

import com.sse.attemper.common.param.CommonParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectGetParam implements CommonParam {

    protected String projectName;

    @Override
    public String validate() {
        if(StringUtils.isBlank(projectName)) {
            return "6600";
        }
        return null;
    }
}
