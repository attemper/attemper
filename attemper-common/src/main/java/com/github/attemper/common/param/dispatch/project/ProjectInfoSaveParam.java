package com.github.attemper.common.param.dispatch.project;

import com.github.attemper.common.enums.UriType;
import com.github.attemper.common.param.CommonParam;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class ProjectInfoSaveParam implements CommonParam {

    protected String projectName;

    protected String uri;

    protected Integer type;

    @Override
    public String validate() {
        if(StringUtils.isBlank(projectName)) {
            return "6600";
        }
        if(StringUtils.isBlank(uri)) {
            return "6603";
        }
        if(UriType.get(type) == null){
            return "6621";
        }
        return null;
    }
}
