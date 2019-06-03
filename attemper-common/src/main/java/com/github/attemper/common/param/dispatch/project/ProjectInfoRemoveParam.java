package com.github.attemper.common.param.dispatch.project;

import com.github.attemper.common.param.CommonParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
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
