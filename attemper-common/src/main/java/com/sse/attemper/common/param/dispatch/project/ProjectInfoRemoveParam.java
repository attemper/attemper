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
public class ProjectInfoRemoveParam implements CommonParam {

    protected String projectName;

    protected String uri;

    @Override
    public String validate() {
        if(StringUtils.isBlank(projectName)) {
            return "6600";
        }
        if(StringUtils.isBlank(uri)) {
            return "6603";
        }
        return null;
    }
}
