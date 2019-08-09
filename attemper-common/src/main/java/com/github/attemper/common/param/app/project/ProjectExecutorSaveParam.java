package com.github.attemper.common.param.app.project;

import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class ProjectExecutorSaveParam implements CommonParam {

    protected String projectName;

    protected List<String> executorUris;

    @Override
    public String validate() {
        if(StringUtils.isBlank(projectName)) {
            return "6500";
        }
        return null;
    }
}
