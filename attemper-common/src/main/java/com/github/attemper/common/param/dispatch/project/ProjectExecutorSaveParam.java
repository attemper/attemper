package com.github.attemper.common.param.dispatch.project;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Getter
@Setter
public class ProjectExecutorSaveParam implements CommonParam {

    protected String projectName;

    protected List<String> executorUris;

    @Override
    public String validate() {
        if(StringUtils.isBlank(projectName)) {
            return "6600";
        }
        return null;
    }
}
